package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.realm.Case;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class RealmBrowserActivity extends AppCompatActivity implements RealmBrowserAdapter.Listener, SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener {

    private static final String EXTRAS_REALM_MODEL_CLASS = "REALM_MODEL_CLASS";
    private DynamicRealm mDynamicRealm;
    private Class<? extends RealmModel> mRealmObjectClass;
    private RealmBrowserAdapter mAdapter;
    private TextView textView;
    private TextView txtColumn1;
    private TextView txtColumn2;
    private TextView txtColumn3;
    private List<Field> tmpSelectedFieldList;
    private List<Field> selectedFieldList;
    private List<Field> fieldsList;
    private AppCompatCheckBox[] checkBoxes;
    private RealmPreferences realmPreferences;
    private DrawerLayout drawerLayout;
    private Snackbar snackbar;
    private AbstractList<? extends DynamicRealmObject> realmObjects;
    private FloatingActionButton fab;


    public static void start(Context context, Class<? extends RealmModel> realmModelClass) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_MODEL_CLASS, realmModelClass);
        context.startActivity(intent);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_browser);

        mDynamicRealm = DynamicRealm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRAS_REALM_MODEL_CLASS)) {
            mRealmObjectClass = (Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS);
            realmObjects = mDynamicRealm.where(mRealmObjectClass.getSimpleName()).findAll();
        } else {
            DynamicRealmObject dynamicRealmObject = RealmHolder.getInstance().getObject();
            Field field = RealmHolder.getInstance().getField();
            realmObjects = dynamicRealmObject.getList(field.getName());
            if (Utils.isParametrizedField(field)) {
                ParameterizedType pType = (ParameterizedType) field.getGenericType();
                Class<?> pTypeClass = (Class<?>) pType.getActualTypeArguments()[0];
                mRealmObjectClass = (Class<? extends RealmObject>) pTypeClass;
            }
        }

        RealmObjectSchema schema = mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());

        selectedFieldList = new ArrayList<>();
        tmpSelectedFieldList = new ArrayList<>();
        fieldsList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                fieldsList.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d("Initializing field map.", e);
            }
        }
        mAdapter = new RealmBrowserAdapter(this, realmObjects, selectedFieldList, this, mDynamicRealm);


        // Init Views
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.realm_browser_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        textView = (TextView) findViewById(R.id.realm_browser_txtIndex);
        txtColumn1 = (TextView) findViewById(R.id.realm_browser_txtColumn1);
        txtColumn2 = (TextView) findViewById(R.id.realm_browser_txtColumn2);
        txtColumn3 = (TextView) findViewById(R.id.realm_browser_txtColumn3);
        fab = (FloatingActionButton) findViewById(R.id.realm_browser_fab);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRAS_REALM_MODEL_CLASS)) {
            fab.setOnClickListener(createFABClickListener((Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS)));
        } else {
            // Currently displaying RealmList of parametrized field => don't give option to add new object
            fab.setVisibility(View.GONE);
        }


        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.realm_browser_ic_menu_white_24dp);
            actionBar.setTitle(String.format("%s", mRealmObjectClass.getSimpleName()));
        }


        // Init Navigation View
        drawerLayout = (DrawerLayout) findViewById(R.id.realm_browser_drawer_layout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.realm_browser_navigationView);
        Menu menu = mNavigationView.getMenu();

        // Init Field Checkboxes
        checkBoxes = new AppCompatCheckBox[fieldsList.size()];
        SubMenu subMenu = menu.addSubMenu("Fields");
        for (int i = 0; i < fieldsList.size(); i++) {
            MenuItem m = subMenu.add(fieldsList.get(i).getName());
            AppCompatCheckBox cb = new AppCompatCheckBox(this);
            cb.setOnCheckedChangeListener(this);
            cb.setTag(i);
            checkBoxes[i] = cb;
            m.setActionView(cb);
        }
        selectDefaultFields();
        updateColumnTitle(selectedFieldList);

        // Init Text Wrapping Switch
        realmPreferences = new RealmPreferences(getApplicationContext());
        MenuItem menuItem = menu.findItem(R.id.realm_browser_action_wrapping);
        SwitchCompat switchCompat = (SwitchCompat) MenuItemCompat.getActionView(menuItem);
        switchCompat.setChecked(realmPreferences.shouldWrapText());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realmPreferences.setShouldWrapText(isChecked);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private View.OnClickListener createFABClickListener(final Class<? extends RealmModel> modelClass) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RealmObjectActivity.getIntent(RealmBrowserActivity.this, modelClass, true));
            }
        };
    }


    @NonNull
    private AbstractList<? extends DynamicRealmObject> filterRealmResults(@NonNull String filter) {
        if (filter.isEmpty()) {
            return realmObjects;
        }

        RealmQuery<? extends DynamicRealmObject> query;
        if (realmObjects instanceof RealmList) {
            query = ((RealmList<? extends DynamicRealmObject>) realmObjects).where();
        } else if (realmObjects instanceof RealmResults) {
            query = mDynamicRealm.where(mRealmObjectClass.getSimpleName());
        } else {
            throw new IllegalArgumentException();
        }

        boolean openedGroup = false;
        for (int i = 0; i < fieldsList.size(); i++) {
            if (fieldsList.get(i).getType().equals(String.class)) {
                // STRING
                if (!openedGroup) {
                    openedGroup = true;
                    query.beginGroup();
                } else {
                    query.or();
                }
                query.contains(fieldsList.get(i).getName(), filter, Case.INSENSITIVE);
            } else if (fieldsList.get(i).getType().equals(Integer.class) || fieldsList.get(i).getType().getName().equals("int")) {
                // INTEGER
                try {
                    int value = Integer.parseInt(filter.trim());
                    if (!openedGroup) {
                        openedGroup = true;
                        query.beginGroup();
                    } else {
                        query.or();
                    }
                    query.equalTo(fieldsList.get(i).getName(), value);
                } catch (NumberFormatException e) {
                }
            }
            // TODO Long, Short, Byte, Double, Float, Date
            if (openedGroup && i == fieldsList.size() - 1) {
                query.endGroup();
            }
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
        if (mDynamicRealm != null) {
            mDynamicRealm.close();
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.realm_browser_action_filter);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRowClicked(@NonNull DynamicRealmObject realmObject) {
        RealmHolder.getInstance().setObject(realmObject);
        Intent intent = RealmObjectActivity.getIntent(this, mRealmObjectClass, false);
        startActivity(intent);
    }


    private void selectDefaultFields() {
        selectedFieldList.clear();
        for (int i = 0; i < fieldsList.size(); i++) {
            if (i < 3) {
                selectedFieldList.add(fieldsList.get(i));
                checkBoxes[i].setChecked(true);
            }
        }
    }


    private void disableCheckBoxes() {
        for (AppCompatCheckBox cb : checkBoxes) {
            if (!cb.isChecked()) {
                cb.setEnabled(false);
            }
        }
    }


    private void enableCheckboxes() {
        for (AppCompatCheckBox cb : checkBoxes) {
            cb.setEnabled(true);
        }
    }


    private void updateColumnTitle(final List<Field> columnsList) {
        textView.setText("#");

        LinearLayout.LayoutParams layoutParams2 = createLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = createLayoutParams();

        if (columnsList.size() > 0) {
            txtColumn1.setText(columnsList.get(0).getName());

            if (columnsList.size() > 1) {
                txtColumn2.setText(columnsList.get(1).getName());
                layoutParams2.weight = 1;

                if (columnsList.size() > 2) {
                    txtColumn3.setText(columnsList.get(2).getName());
                    layoutParams3.weight = 1;
                } else {
                    layoutParams3.weight = 0;
                }
            } else {
                layoutParams2.weight = 0;
            }
        } else {
            txtColumn1.setText(null);
        }

        txtColumn2.setLayoutParams(layoutParams2);
        txtColumn3.setLayoutParams(layoutParams3);
    }


    private LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(final String newText) {
        AbstractList<? extends DynamicRealmObject> results = filterRealmResults(newText);
        mAdapter.setRealmList(results);
        mAdapter.notifyDataSetChanged();
        if (newText.isEmpty() && snackbar != null) {
            snackbar.dismiss();
        } else {
            snackbar = Snackbar.make(drawerLayout, String.format("%d entries found.", results.size()), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
        return true;
    }


    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        int fieldIndex = (int) buttonView.getTag();
        Field field = fieldsList.get(fieldIndex);
        if (isChecked && !tmpSelectedFieldList.contains(field)) {
            tmpSelectedFieldList.add(field);
        } else if (tmpSelectedFieldList.contains(field)) {
            tmpSelectedFieldList.remove(field);
        }
        if (tmpSelectedFieldList.size() > 2) {
            disableCheckBoxes();
        } else {
            enableCheckboxes();
        }
        selectedFieldList.clear();
        selectedFieldList.addAll(tmpSelectedFieldList);
        updateColumnTitle(selectedFieldList);
        mAdapter.setFieldList(selectedFieldList);
        mAdapter.notifyDataSetChanged();
    }
}
