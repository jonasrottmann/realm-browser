package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.browser.BrowserContract;
import de.jonasrottmann.realmbrowser.browser.view.RealmBrowserActivity;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.helper.Utils;
import de.jonasrottmann.realmbrowser.object.ObjectContract;
import de.jonasrottmann.realmbrowser.object.ObjectPresenter;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;
import io.realm.RealmObjectSchema;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import timber.log.Timber;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_FIELD;
import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RealmObjectActivity extends AppCompatActivity implements ObjectContract.View {
    private static final String EXTRAS_FLAG_NEW_OBJECT = "NEW_OBJECT";

    private ObjectContract.Presenter presenter;
    @Nullable
    private DynamicRealm dynamicRealm;

    private DynamicRealmObject currentDynamicRealmObject;
    private List<Field> classFields;
    private RealmBrowserViewField primaryKeyFieldView;
    private HashMap<String, RealmBrowserViewField> fieldViewsList;

    private LinearLayout linearLayout;
    private Menu optionsMenu;

    public static Intent getIntent(Context context, boolean newObject) {
        Intent intent = new Intent(context, RealmObjectActivity.class);
        intent.putExtra(EXTRAS_FLAG_NEW_OBJECT, newObject);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_object);

        RealmConfiguration configuration = (RealmConfiguration) DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG);
        if (configuration != null) dynamicRealm = DynamicRealm.getInstance(configuration);

        // Get Extra
        if (!getIntent().getBooleanExtra(EXTRAS_FLAG_NEW_OBJECT, true)) {
            currentDynamicRealmObject = (DynamicRealmObject) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_OBJECT);
        }

        // Fill fields list
        RealmObjectSchema schema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());
        classFields = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                classFields.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d(e, "Initializing field map.");
            }
        }

        // Init Views
        linearLayout = (LinearLayout) findViewById(R.id.realm_browser_linearLayout);
        fieldViewsList = new HashMap<>();
        int margin = this.getResources().getDimensionPixelSize(R.dimen.realm_browser_activity_margin);

        for (final Field field : classFields) {
            RealmBrowserViewField realmFieldView;
            if (Utils.isString(field)) {
                realmFieldView = new RealmBrowserViewString(this, schema, field);
            } else if (Utils.isNumber(field)) {
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
                        if (currentDynamicRealmObject != null) {
                            DataHolder.getInstance().save(DATA_HOLDER_KEY_OBJECT, currentDynamicRealmObject);
                            DataHolder.getInstance().save(DATA_HOLDER_KEY_FIELD, field);
                            RealmBrowserActivity.start(RealmObjectActivity.this, BrowserContract.DisplayMode.REALM_LIST);
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
                        if (currentDynamicRealmObject != null) {
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
            realmFieldView.setPadding(margin, margin / 2, margin, margin / 2);

            if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isPrimaryKey(field.getName())) {
                primaryKeyFieldView = realmFieldView;
            }

            // Add the object to the view for setting the current value etc.
            if (currentDynamicRealmObject != null) {
                realmFieldView.setRealmObject(currentDynamicRealmObject);
            }

            // Add View
            linearLayout.addView(realmFieldView);
            fieldViewsList.put(field.getName(), realmFieldView);
        }

        // Set Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Presenter
        attachPresenter((ObjectContract.Presenter) getLastCustomNonConfigurationInstance());
        this.presenter.requestForContentUpdate(dynamicRealm);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_objectactivity, menu);
        this.optionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_save) {
            if (saveObject()) {
                Snackbar.make(linearLayout, "Saved Changes.", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_delete) {
            presenter.onDeleteObjectActionSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dynamicRealm != null && !dynamicRealm.isClosed()) {
            dynamicRealm.close();
        }
    }

    @Nullable
    private DynamicRealmObject createObject() {
        if (dynamicRealm == null) return null;

        DynamicRealmObject newRealmObject = null;

        // Start Realm Transaction
        dynamicRealm.beginTransaction();

        // Create object
        if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            try {
                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
                newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName(), fieldViewsList.get(primaryKeyFieldName).getValue());
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
            newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }

        // Commit Realm Transaction
        if (dynamicRealm.isInTransaction()) {
            dynamicRealm.commitTransaction();
        }

        return newRealmObject;
    }

    private boolean saveObject() {
        if (dynamicRealm == null) {
            Timber.e("No realm instance.");
            return false;
        }

        // Return if any field holds a invalid value
        for (String fieldName : fieldViewsList.keySet()) {
            if (!fieldViewsList.get(fieldName).isInputValid()) {
                return false;
            }
        }

        RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());

        DynamicRealmObject newRealmObject = null;
        if (currentDynamicRealmObject == null || (realmObjectSchema.hasPrimaryKey() && (!Utils.equals(currentDynamicRealmObject.get(realmObjectSchema.getPrimaryKey()), primaryKeyFieldView.getValue())))) {
            // PK has been changed or don't have and old object to change -> create new object
            newRealmObject = createObject();
            if (newRealmObject == null) {
                return false;
            }
        }

        dynamicRealm.beginTransaction();
        // Set values
        for (String fieldName : fieldViewsList.keySet()) {
            if (realmObjectSchema.isPrimaryKey(fieldName)) {
                continue; // Prevent changing of PK, should be handled with creation of new object with new PK
            }
            if (!realmObjectSchema.isNullable(fieldName) && fieldViewsList.get(fieldName).getValue() == null) {
                throw new IllegalStateException("A view which holds a nonnullable field must not return null.");
            }
            if (newRealmObject == null) {
                currentDynamicRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
            } else {
                newRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
            }
        }

        // Delete old object if new object was created
        if (newRealmObject != null && currentDynamicRealmObject != null && currentDynamicRealmObject.isManaged()) {
            currentDynamicRealmObject.deleteFromRealm();
            currentDynamicRealmObject = newRealmObject;
        }

        // Update views
        for (RealmBrowserViewField viewField : this.fieldViewsList.values()) {
            viewField.setRealmObject(currentDynamicRealmObject);
        }

        dynamicRealm.commitTransaction();

        // Reset primary key error
        RealmBrowserViewField primaryKeyView = fieldViewsList.get(Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName())));
        if (primaryKeyView != null) {
            primaryKeyView.togglePrimaryKeyError(false);
        }

        return true;
    }

    //region ViewInput
    @Override
    public void attachPresenter(@Nullable ObjectContract.Presenter presenter) {
        this.presenter = presenter;
        if (this.presenter == null) {
            this.presenter = new ObjectPresenter();
        }
        this.presenter.attachView(this);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void updateWithTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void updateWithDeleteActionShown(boolean shown) {
        optionsMenu.findItem(R.id.realm_browser_action_delete).setVisible(shown);
    }
    //endregion
}
