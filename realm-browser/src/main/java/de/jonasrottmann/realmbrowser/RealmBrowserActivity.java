package de.jonasrottmann.realmbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import de.jonasrottmann.realmbrowser.utils.MagicUtils;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;

public class RealmBrowserActivity extends AppCompatActivity implements RealmAdapter.Listener, SearchView.OnQueryTextListener {

    private static final String EXTRAS_REALM_FILE_NAME = "EXTRAS_REALM_FILE_NAME";
    private static final String EXTRAS_REALM_MODEL_INDEX = "REALM_MODEL_INDEX";

    private Realm mRealm;
    private Class<? extends RealmObject> mRealmObjectClass;
    private RealmAdapter mAdapter;
    private TextView mTxtIndex;
    private TextView mTxtColumn1;
    private TextView mTxtColumn2;
    private TextView mTxtColumn3;
    private List<Field> mTmpSelectedFieldList;
    private List<Field> mSelectedFieldList;
    private List<Field> mFieldsList;



    public static void start(Activity activity, int realmModelIndex, String realmFileName) {
        Intent intent = new Intent(activity, RealmBrowserActivity.class);
        intent.putExtra(EXTRAS_REALM_MODEL_INDEX, realmModelIndex);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        activity.startActivity(intent);
    }



    public static void start(Activity activity, String realmFileName) {
        Intent intent = new Intent(activity, RealmBrowserActivity.class);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        activity.startActivity(intent);
    }



    public static void start(Context context, int realmModelIndex, String realmFileName) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_MODEL_INDEX, realmModelIndex);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        context.startActivity(intent);
    }



    public static void start(Context context, String realmFileName) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_realm_browser);

        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(realmFileName)
                .build();
        mRealm = Realm.getInstance(config);

        AbstractList<? extends RealmObject> realmObjects;

        if (getIntent().getExtras().containsKey(EXTRAS_REALM_MODEL_INDEX)) {
            int index = getIntent().getIntExtra(EXTRAS_REALM_MODEL_INDEX, 0);
            mRealmObjectClass = RealmBrowser.getInstance().getRealmModelList().get(index);
            realmObjects = mRealm.allObjects(mRealmObjectClass);
        } else {
            RealmObject object = RealmHolder.getInstance().getObject();
            Field field = RealmHolder.getInstance().getField();
            String methodName = MagicUtils.createRealmGetterMethodName(field);
            realmObjects = MagicUtils.invokeMethodForRealmResult(object, methodName);
            if (MagicUtils.isParameterizedField(field)) {
                ParameterizedType pType = (ParameterizedType) field.getGenericType();
                Class<?> pTypeClass = (Class<?>) pType.getActualTypeArguments()[0];
                mRealmObjectClass = (Class<? extends RealmObject>) pTypeClass;
            }
        }

        mSelectedFieldList = new ArrayList<>();
        mTmpSelectedFieldList = new ArrayList<>();
        mFieldsList = new ArrayList<>();

        // Populate fields list
        for (int i = 0; i < mRealmObjectClass.getDeclaredFields().length; i++) {
            Field f = mRealmObjectClass.getDeclaredFields()[i];
            if (!(Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))) // Ignore constant static fields
                mFieldsList.add(f);
        }

        mAdapter = new RealmAdapter(this, realmObjects, mSelectedFieldList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mTxtIndex = (TextView) findViewById(R.id.txtIndex);
        mTxtColumn1 = (TextView) findViewById(R.id.txtColumn1);
        mTxtColumn2 = (TextView) findViewById(R.id.txtColumn2);
        mTxtColumn3 = (TextView) findViewById(R.id.txtColumn3);

        selectDefaultFields();
        updateColumnTitle(mSelectedFieldList);
    }


    @NonNull
    private AbstractList<? extends RealmObject> filterRealmResults(@NonNull String filter) {
        if (filter.isEmpty())
            return mRealm.allObjects(mRealmObjectClass);

        boolean openedGroup = false;
        RealmQuery<? extends RealmObject> query = mRealm.where(mRealmObjectClass);
        for (int i = 0; i < mFieldsList.size(); i++) {
            if (mFieldsList.get(i).getType().equals(String.class)) {
                // STRING
                if (!openedGroup) {
                    openedGroup = true;
                    query.beginGroup();
                } else
                    query.or();
                query.contains(mFieldsList.get(i).getName(), filter, Case.INSENSITIVE);
            } else if (mFieldsList.get(i).getType().equals(Integer.class) || mFieldsList.get(i).getType().getName().equals("int")) {
                // INTEGER
                try {
                    int value = Integer.parseInt(filter.trim());
                    if (!openedGroup) {
                        openedGroup = true;
                        query.beginGroup();
                    } else
                        query.or();
                    query.equalTo(mFieldsList.get(i).getName(), value);
                } catch (NumberFormatException e) {}
            }
            // TODO Long, Short, Byte, Double, Float, Date
            if (openedGroup && i == mFieldsList.size() - 1)
                query.endGroup();
        }
        return query.findAll();
    }



    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }



    @Override
    protected void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }
        super.onDestroy();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_columns) {
            showColumnsDialog();
        }
        if (id == R.id.action_settings) {
            SettingsActivity.start(this);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.menu_filter);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        if (getIntent().getExtras().containsKey(EXTRAS_REALM_MODEL_INDEX)) {
            searchView.setOnQueryTextListener(this);
        } else {
            searchMenuItem.setEnabled(false);
        }

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onRowItemClicked(@NonNull RealmObject realmObject, @NonNull Field field) {
        RealmHolder.getInstance().setObject(realmObject);
        RealmHolder.getInstance().setField(field);
        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);
        RealmBrowserActivity.start(this, realmFileName);
    }



    private void selectDefaultFields() {
        mSelectedFieldList.clear();
        for (Field field : mFieldsList) {
            if (mSelectedFieldList.size() < 3) {
                mSelectedFieldList.add(field);
            }
        }
    }



    private void updateColumnTitle(List<Field> columnsList) {
        mTxtIndex.setText("#");

        LinearLayout.LayoutParams layoutParams2 = createLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = createLayoutParams();

        if (columnsList.size() > 0) {
            mTxtColumn1.setText(columnsList.get(0).getName());

            if (columnsList.size() > 1) {
                mTxtColumn2.setText(columnsList.get(1).getName());
                layoutParams2.weight = 1;

                if (columnsList.size() > 2) {
                    mTxtColumn3.setText(columnsList.get(2).getName());
                    layoutParams3.weight = 1;
                } else {
                    layoutParams3.weight = 0;
                }
            } else {
                layoutParams2.weight = 0;
            }
        }

        mTxtColumn2.setLayoutParams(layoutParams2);
        mTxtColumn3.setLayoutParams(layoutParams3);
    }



    private LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    private void showColumnsDialog() {
        final String[] items = new String[mFieldsList.size()];
        for (int i = 0; i < items.length; i++) {
            Field field = mFieldsList.get(i);
            items[i] = field.getName();
        }

        boolean[] checkedItems = new boolean[mFieldsList.size()];
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = mSelectedFieldList.contains(mFieldsList.get(i));
        }

        mTmpSelectedFieldList.clear();
        mTmpSelectedFieldList.addAll(mSelectedFieldList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Columns to display");
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        Field field = mFieldsList.get(indexSelected);
                        if (isChecked) {
                            mTmpSelectedFieldList.add(field);
                        } else if (mTmpSelectedFieldList.contains(field)) {
                            mTmpSelectedFieldList.remove(field);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mTmpSelectedFieldList.isEmpty()) {
                            selectDefaultFields();
                        } else {
                            mSelectedFieldList.clear();
                            mSelectedFieldList.addAll(mTmpSelectedFieldList);
                        }
                        updateColumnTitle(mSelectedFieldList);
                        mAdapter.setFieldList(mSelectedFieldList);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.setRealmList(filterRealmResults(newText));
        mAdapter.notifyDataSetChanged();
        return true;
    }
}
