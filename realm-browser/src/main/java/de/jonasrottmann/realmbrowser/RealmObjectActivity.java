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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.jonasrottmann.realmbrowser.utils.RealmHolder;
import de.jonasrottmann.realmbrowser.utils.Utils;
import de.jonasrottmann.realmbrowser.views.BlobView;
import de.jonasrottmann.realmbrowser.views.BoolView;
import de.jonasrottmann.realmbrowser.views.DateView;
import de.jonasrottmann.realmbrowser.views.FieldView;
import de.jonasrottmann.realmbrowser.views.NumberView;
import de.jonasrottmann.realmbrowser.views.RealmListView;
import de.jonasrottmann.realmbrowser.views.RealmObjectView;
import de.jonasrottmann.realmbrowser.views.StringView;
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
    private static final String EXTRAS_FLAG_NEW_OBJECT = "NEW_OBJECT";
    private Class<? extends RealmModel> mRealmObjectClass;
    private DynamicRealmObject mDynamicRealmObject;
    private List<Field> mFieldsList;
    private HashMap<String, FieldView> mFieldViewsList;
    private DynamicRealm mDynamicRealm;
    private LinearLayout mLinearLayout;

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
        mDynamicRealm = DynamicRealm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        mRealmObjectClass = (Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS);

        if (!getIntent().getBooleanExtra(EXTRAS_FLAG_NEW_OBJECT, true)) {
            mDynamicRealmObject = RealmHolder.getInstance().getObject();
        }

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
        mLinearLayout = (LinearLayout) findViewById(R.id.realm_browser_linearLayout);
        mFieldViewsList = new HashMap<>();
        int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        for (final Field field : mFieldsList) {
            final FieldView realmFieldView;

            if (Utils.isString(field)) {
                realmFieldView = new StringView(this, schema, field);
            } else if (Utils.isNumberField(field)) {
                realmFieldView = new NumberView(this, schema, field);
            } else if (Utils.isBoolean(field)) {
                realmFieldView = new BoolView(this, schema, field);
            } else if (Utils.isBlob(field)) {
                realmFieldView = new BlobView(this, schema, field);
            } else if (Utils.isDate(field)) {
                realmFieldView = new DateView(this, schema, field);
            } else if (Utils.isParametrizedField(field)) {
                realmFieldView = new RealmListView(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mDynamicRealmObject != null) {
                            RealmHolder.getInstance().setObject(mDynamicRealmObject);
                            RealmHolder.getInstance().setField(field);
                            RealmBrowserActivity.start(RealmObjectActivity.this);
                        } else {
                            // TODO choose objects to add
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (Utils.isRealmObjectField(field)) {
                realmFieldView = new RealmObjectView(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mDynamicRealmObject != null) {
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

            if (mDynamicRealmObject != null) {
                realmFieldView.setRealmObject(mDynamicRealmObject);
            }

            mLinearLayout.addView(realmFieldView);

            mFieldViewsList.put(field.getName(), realmFieldView);
        }

        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mDynamicRealmObject == null)
                actionBar.setTitle(String.format("New %s", mRealmObjectClass.getSimpleName()));
            else
                actionBar.setTitle(String.format("%s", mRealmObjectClass.getSimpleName()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_objectactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDynamicRealmObject == null) {
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
            if (saveObject(mDynamicRealmObject)) {
                Snackbar.make(mLinearLayout, "Saved Changes.", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_delete) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete");
            builder.setMessage(String.format("Are you sure you want to delete this %s object?", mRealmObjectClass.getSimpleName()));
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDynamicRealm.beginTransaction();
                    mDynamicRealmObject.deleteFromRealm();
                    mDynamicRealm.commitTransaction();
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
        mDynamicRealm.close();
    }

    @Nullable
    private DynamicRealmObject createObject() {
        DynamicRealmObject realmObject = null;

        // Start Realm Transaction
        mDynamicRealm.beginTransaction();

        // Create object
        if (mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            // TODO show exceptions to user
            try {
                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
                realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName(), mFieldViewsList.get(primaryKeyFieldName).getValue());
            } catch (IllegalArgumentException e) {
                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
                mDynamicRealm.cancelTransaction();
                Snackbar.make(mLinearLayout, "Error creating Object: IllegalArgumentException", Snackbar.LENGTH_SHORT).show();
            } catch (RealmPrimaryKeyConstraintException e) {
                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
                mFieldViewsList.get(Utils.getPrimaryKeyFieldName(mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
                mDynamicRealm.cancelTransaction();
                Snackbar.make(mLinearLayout, "Error creating Object: PrimaryKeyConstraintException", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            realmObject = mDynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }

        // Commit Realm Transaction
        if (mDynamicRealm.isInTransaction()) mDynamicRealm.commitTransaction();

        mDynamicRealmObject = realmObject;

        return realmObject;
    }


    private boolean saveObject(@Nullable DynamicRealmObject realmObject) {
        // Return if any field holds a invalid value
        for (String fieldName : mFieldViewsList.keySet()) {
            if (!mFieldViewsList.get(fieldName).isInputValid()) return false;
        }

        if (realmObject == null)
            realmObject = createObject();
        if (realmObject == null)
            return false;

        // Start Realm Transaction
        mDynamicRealm.beginTransaction();

        // Set values
        for (String fieldName : mFieldViewsList.keySet()) {
            if (!mDynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isNullable(fieldName) && mFieldViewsList.get(fieldName).getValue() == null) {
                // TODO: prevent setting null to list fields
                continue;
            }
            realmObject.set(mFieldViewsList.get(fieldName).getField().getName(), mFieldViewsList.get(fieldName).getValue());
        }

        // Commit Realm Transaction
        mDynamicRealm.commitTransaction();

        return true;
    }
}
