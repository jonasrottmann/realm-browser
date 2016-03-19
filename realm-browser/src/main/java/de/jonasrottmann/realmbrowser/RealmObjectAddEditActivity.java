package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.jonasrottmann.realmbrowser.utils.L;
import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Jonas Rottmann on 15/03/16.
 */
public class RealmObjectAddEditActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_FILE_NAME = "EXTRAS_REALM_FILE_NAME";
    private static final String EXTRAS_REALM_MODEL_INDEX = "REALM_MODEL_INDEX";
    private Class<? extends RealmObject> mRealmObjectClass;
    private List<Field> mFieldsList;
    private HashMap<String, RealmAddEditFieldView> mFieldViewsList;
    private DynamicRealm mDynamicRealm;

    public static void start(Context context, int realmModelIndex, String realmFileName) {
        Intent intent = new Intent(context, RealmObjectAddEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_MODEL_INDEX, realmModelIndex);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_addedit);

        int index = getIntent().getIntExtra(EXTRAS_REALM_MODEL_INDEX, 0);
        mRealmObjectClass = RealmBrowser.getInstance().getRealmModelList().get(index);

        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(realmFileName)
                .build();
        mDynamicRealm = DynamicRealm.getInstance(config);
        RealmObjectSchema schema = mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());

        mFieldsList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                mFieldsList.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                L.d("Initializing field map.", e);
            }
        }


        // Init Views
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.realm_browser_addedit_linearLayout);
        mFieldViewsList = new HashMap<>();
        for (Field field : mFieldsList) {
            // TODO inflate layout
            RealmAddEditFieldView addEditFieldView = new RealmAddEditFieldView(this);
            addEditFieldView.setField(schema, field);
            linearLayout.addView(addEditFieldView);
            mFieldViewsList.put(field.getName(), addEditFieldView);
        }

        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(String.format("New %s", mRealmObjectClass.getSimpleName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_addedit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_save) {
            createObject();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDynamicRealm.close();
    }


    private void createObject() {
        mFieldViewsList.get(Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(false);

        DynamicRealmObject realmObject = null;

        mDynamicRealm.beginTransaction();

        // Create object
        if (mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            try {
                realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName(), mFieldViewsList.get(Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).getValue());
            } catch (IllegalArgumentException e) {
                mDynamicRealm.cancelTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                mFieldViewsList.get(Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
                mDynamicRealm.cancelTransaction();
                return;
            }
        } else {
            realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }
        // Set values
        if (realmObject != null) {
            for (String fieldName : mFieldViewsList.keySet()) {
                if (mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isNullable(mFieldViewsList.get(fieldName).getField().getName()) || mFieldViewsList.get(fieldName).getValue() != null) {
                    realmObject.set(mFieldViewsList.get(fieldName).getField().getName(), mFieldViewsList.get(fieldName).getValue());
                }
            }
        }

        mDynamicRealm.commitTransaction();

        finish();
    }
}
