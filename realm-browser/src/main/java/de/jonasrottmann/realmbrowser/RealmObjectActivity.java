package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmObjectSchema;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import timber.log.Timber;

/**
 * Created by Jonas Rottmann on 15/03/16.
 */
public class RealmObjectActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_MODEL_CLASS = "REALM_MODEL_CLASS";
    private Class<? extends RealmModel> mRealmObjectClass;
    private List<Field> mFieldsList;
    private HashMap<String, RealmAddEditFieldView> mFieldViewsList;
    private DynamicRealm mDynamicRealm;



    public static Intent getIntent(Context context, Class<? extends RealmModel> realmModelClass) {
        Intent intent = new Intent(context, RealmObjectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_MODEL_CLASS, realmModelClass);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_addedit);

        mRealmObjectClass = (Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS);
        mDynamicRealm = DynamicRealm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        RealmObjectSchema schema = mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());

        mFieldsList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                mFieldsList.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d("Initializing field map.", e);
            }
        }

        // Init Views
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.realm_browser_addedit_linearLayout);
        mFieldViewsList = new HashMap<>();
        int margin16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        for (Field field : mFieldsList) {
            RealmAddEditFieldView addEditFieldView = new RealmAddEditFieldView(this);
            addEditFieldView.setField(schema, field);
            addEditFieldView.setPadding(margin16, margin16 / 2, margin16, margin16 / 2);
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
        // Return if any field holds a invalid value
        for (String fieldName : mFieldViewsList.keySet()) {
            if (!mFieldViewsList.get(fieldName).isValid()) return;
        }


        // Start Realm Transaction
        mDynamicRealm.beginTransaction();
        DynamicRealmObject realmObject;

        // Create object
        if (mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            try {
                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
                realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName(), mFieldViewsList.get(primaryKeyFieldName).getValue());
            } catch (IllegalArgumentException e) {
                // TODO
                mDynamicRealm.cancelTransaction();
                return;
            } catch (RealmPrimaryKeyConstraintException e) {
                mFieldViewsList.get(Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
                mDynamicRealm.cancelTransaction();
                return;
            }
        } else {
            realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }

        // Set values
        for (String fieldName : mFieldViewsList.keySet()) {
            if (!mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isNullable(fieldName) && mFieldViewsList.get(fieldName).getValue() == null) {
                // prevent setting null to list fields
                continue;
            }
            realmObject.set(mFieldViewsList.get(fieldName).getField().getName(), mFieldViewsList.get(fieldName).getValue());
        }

        // Commit Realm Transaction
        mDynamicRealm.commitTransaction();

        // Finish Activity
        finish();
    }
}
