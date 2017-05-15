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
import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;
import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmObjectSchema;
import timber.log.Timber;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_FIELD;
import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RealmObjectActivity extends AppCompatActivity implements ObjectContract.View {
    private static final String EXTRAS_FLAG_NEW_OBJECT = "NEW_OBJECT";

    @Nullable
    private ObjectContract.Presenter presenter;
    @Nullable
    private DynamicRealm dynamicRealm;
    @Nullable
    private Menu optionsMenu;
    @Nullable
    private LinearLayout linearLayout;

    private List<Field> classFields;
    private RealmBrowserViewField primaryKeyFieldView;

    @NonNull
    private HashMap<FieldViewPojo, RealmBrowserViewField> fieldViewsMap = new HashMap<>();

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

        // Init Views
        linearLayout = (LinearLayout) findViewById(R.id.realm_browser_linearLayout);

        // Set Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Presenter
        attachPresenter((ObjectContract.Presenter) getLastCustomNonConfigurationInstance());
        if (this.presenter == null) {
            throw new IllegalStateException("No presenter present.");
        }
        this.presenter.requestForContentUpdate(dynamicRealm, getIntent().getBooleanExtra(EXTRAS_FLAG_NEW_OBJECT, true));
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
            presenter.onSaveObjectActionSelected();
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
    public void updateWithFieldViewPojos(@NonNull ArrayList<FieldViewPojo> fieldViewPojos) {
        int margin = this.getResources().getDimensionPixelSize(R.dimen.realm_browser_activity_margin);

        for (FieldViewPojo pojo : fieldViewPojos) {
            RealmBrowserViewField realmFieldView;
            if (Utils.isString(pojo.getField())) {
                realmFieldView = new RealmBrowserViewString(this, schema, field);
            } else if (Utils.isNumber(pojo.getField())) {
                realmFieldView = new RealmBrowserViewNumber(this, schema, field);
            } else if (Utils.isBoolean(pojo.getField())) {
                realmFieldView = new RealmBrowserViewBool(this, schema, field);
            } else if (Utils.isBlob(pojo.getField())) {
                realmFieldView = new RealmBrowserViewBlob(this, schema, field);
            } else if (Utils.isDate(pojo.getField())) {
                realmFieldView = new RealmBrowserViewDate(this, schema, field);
            } else if (Utils.isParametrizedField(pojo.getField())) {
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
            } else if (Utils.isRealmObjectField(pojo.getField())) {
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

            // Add the object to the view for setting the current value etc.
            if (currentDynamicRealmObject != null) {
                realmFieldView.setRealmObject(currentDynamicRealmObject);
            }

            // Add View
            if (linearLayout != null) {
                linearLayout.addView(realmFieldView);
            }
            this.fieldViewsMap.put(pojo, realmFieldView);
        }
    }

    @Override
    public void updateWithFieldViewPojo(@NonNull FieldViewPojo fieldViewPojo) {
        RealmBrowserViewField view = fieldViewsMap.get(fieldViewPojo);
        view.togglePrimaryKeyError(fieldViewPojo.isPrimaryKeyError());
        // TODO update view...
        this.fieldViewsMap.put(fieldViewPojo, view);
    }

    @Override
    public void updateWithTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void updateWithDeleteActionShown(boolean shown) {
        if (optionsMenu != null) {
            optionsMenu.findItem(R.id.realm_browser_action_delete).setVisible(shown);
        }
    }

    @Override
    public void showSavedSuccessfully() {
        if (linearLayout != null) {
            Snackbar.make(linearLayout, "Saved Changes.", Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saved Changes.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
}
