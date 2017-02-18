package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmObjectSchema;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import timber.log.Timber;

/**
 * Created by Jonas Rottmann on 15/03/16.
 */
public class RealmObjectActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_MODEL_CLASS = "REALM_MODEL_CLASS";
    private static final String EXTRAS_FLAG_NEW_OBJECT = "NEW_OBJECT";
    private Class<? extends RealmModel> mRealmObjectClass;
    private DynamicRealmObject dynamicRealmObject;
    private List<Field> fieldList;
    private HashMap<String, RealmBrowserViewField> fieldViewsList;
    private DynamicRealm dynamicRealm;
    private LinearLayout linearLayout;

    public static Intent getIntent(Context context, Class<? extends RealmModel> realmModelClass, boolean newObject) {
        Intent intent = new Intent(context, RealmObjectActivity.class);
        intent.putExtra(EXTRAS_REALM_MODEL_CLASS, realmModelClass);
        intent.putExtra(EXTRAS_FLAG_NEW_OBJECT, newObject);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_object);
        dynamicRealm = DynamicRealm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        mRealmObjectClass = (Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS);

        if (!getIntent().getBooleanExtra(EXTRAS_FLAG_NEW_OBJECT, true)) {
            dynamicRealmObject = RealmHolder.getInstance().getObject();
        }

        RealmObjectSchema schema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());
        fieldList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                fieldList.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d("Initializing field map.", e);
            }
        }

        // Init Views
        linearLayout = (LinearLayout) findViewById(R.id.realm_browser_linearLayout);
        fieldViewsList = new HashMap<>();
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        for (final Field field : fieldList) {
            RealmBrowserViewField realmFieldView;

            if (Utils.isString(field)) {
                realmFieldView = new RealmBrowserViewString(this, schema, field);
            } else if (Utils.isNumberField(field)) {
                realmFieldView = new RealmBrowserViewNumber(this, schema, field);
            } else if (Utils.isBoolean(field)) {
                realmFieldView = new RealmBrowserViewBool(this, schema, field);
            } else if (Utils.isBlob(field)) {
                realmFieldView = new RealmBrowserViewBlob(this, schema, field);
            } else if (Utils.isDate(field)) {
                realmFieldView = new RealmBrowserViewDate(this, schema, field);
            } else if (Utils.isParametrizedField(field)) {
                realmFieldView = new RealmBrowserViewRealmList(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dynamicRealmObject != null) {
                            RealmHolder.getInstance().setObject(dynamicRealmObject);
                            RealmHolder.getInstance().setField(field);
                            RealmBrowserActivity.start(RealmObjectActivity.this);
                        } else {
                            // TODO choose objects to add
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (Utils.isRealmObjectField(field)) {
                realmFieldView = new RealmBrowserViewRealmObject(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dynamicRealmObject != null) {
                            // TODO start this activity
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO choose object
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Skip this field.
                continue;
            }

            realmFieldView.setPadding(dp16, dp16 / 2, dp16, dp16 / 2);

            if (dynamicRealmObject != null) {
                realmFieldView.setRealmObject(dynamicRealmObject);
            }

            linearLayout.addView(realmFieldView);

            fieldViewsList.put(field.getName(), realmFieldView);
        }

        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (dynamicRealmObject == null) {
                actionBar.setTitle(String.format("New %s", mRealmObjectClass.getSimpleName()));
            } else {
                actionBar.setTitle(String.format("%s", mRealmObjectClass.getSimpleName()));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_objectactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (dynamicRealmObject == null) {
            menu.findItem(R.id.realm_browser_action_delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_save) {
            if (saveObject(dynamicRealmObject)) {
                Snackbar.make(linearLayout, "Saved Changes.", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_delete) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete");
            builder.setMessage(String.format("Are you sure you want to delete this %s object?", mRealmObjectClass.getSimpleName()));
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dynamicRealm.beginTransaction();
                    dynamicRealmObject.deleteFromRealm();
                    dynamicRealm.commitTransaction();
                    dialogInterface.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dynamicRealm.close();
    }

    @Nullable
    private DynamicRealmObject createObject() {
        DynamicRealmObject realmObject = null;

        // Start Realm Transaction
        dynamicRealm.beginTransaction();

        // Create object
        if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            // TODO show exceptions to user
            try {
                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
                realmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName(), fieldViewsList.get(primaryKeyFieldName).getValue());
            } catch (IllegalArgumentException e) {
                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
                dynamicRealm.cancelTransaction();
                Snackbar.make(linearLayout, "Error creating Object: IllegalArgumentException", Snackbar.LENGTH_SHORT).show();
            } catch (RealmPrimaryKeyConstraintException e) {
                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
                fieldViewsList.get(Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
                dynamicRealm.cancelTransaction();
                Snackbar.make(linearLayout, "Error creating Object: PrimaryKeyConstraintException", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            realmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }

        // Commit Realm Transaction
        if (dynamicRealm.isInTransaction()) {
            dynamicRealm.commitTransaction();
        }

        dynamicRealmObject = realmObject;

        return realmObject;
    }


    private boolean saveObject(@Nullable DynamicRealmObject realmObject) {
        // Return if any field holds a invalid value
        for (String fieldName : fieldViewsList.keySet()) {
            if (!fieldViewsList.get(fieldName).isInputValid()) {
                return false;
            }
        }

        if (realmObject == null) {
            realmObject = createObject();
        }
        if (realmObject == null) {
            return false;
        }

        // Start Realm Transaction
        dynamicRealm.beginTransaction();

        // Set values
        for (String fieldName : fieldViewsList.keySet()) {
            if (!dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isNullable(fieldName) && fieldViewsList.get(fieldName).getValue() == null) {
                // TODO: prevent setting null to list fields
                continue;
            }
            realmObject.set(fieldViewsList.get(fieldName).getField().getName(), fieldViewsList.get(fieldName).getValue());
        }

        // Commit Realm Transaction
        dynamicRealm.commitTransaction();

        return true;
    }
}
