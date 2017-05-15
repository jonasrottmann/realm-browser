package de.jonasrottmann.realmbrowser.browser.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.jonasrottmann.realmbrowser.BuildConfig;
import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.browser.BrowserContract;
import de.jonasrottmann.realmbrowser.browser.BrowserPresenter;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RealmBrowserActivity extends AppCompatActivity implements RealmBrowserAdapter.Listener, NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener, BrowserContract.View {
    private static final String EXTRAS_DISPLAY_MODE = "DISPLAY_MODE";
    private static final int SELECTION_RESULT_REQUEST_CODE = 9294;

    private BrowserContract.Presenter presenter;

    @Nullable
    private DynamicRealm dynamicRealm;
    private RealmBrowserAdapter mAdapter;
    private TextView textView;
    private TextView txtColumn1;
    private TextView txtColumn2;
    private TextView txtColumn3;
    private AppCompatCheckBox[] checkBoxes = null;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private SwitchCompat wrapTextSwitch;

    public static void start(@NonNull Context context, @BrowserContract.DisplayMode int displayMode) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_DISPLAY_MODE, displayMode);
        context.startActivity(intent);
    }

    public static void startForSelection(@NonNull Activity context) {
        Intent intent = new Intent(context, RealmBrowserActivity.class);
        intent.putExtra(EXTRAS_DISPLAY_MODE, BrowserContract.DisplayMode.REALM_CLASS);
        context.startActivityForResult(intent, SELECTION_RESULT_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_browser);

        RealmConfiguration configuration = (RealmConfiguration) DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG);
        if (configuration != null) dynamicRealm = DynamicRealm.getInstance(configuration);

        mAdapter = new RealmBrowserAdapter(this, new RealmList<DynamicRealmObject>(), new ArrayList<Field>(), this, false);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNewObjectSelected();
            }
        });

        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.realm_browser_ic_menu_white_24dp);
        }

        // Init Navigation View
        drawerLayout = (DrawerLayout) findViewById(R.id.realm_browser_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.realm_browser_navigationView);
        wrapTextSwitch = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.realm_browser_action_wrapping));
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem about = navigationView.getMenu().findItem(R.id.realm_browser_action_about);
        about.setTitle(String.format("%s: %s", this.getString(R.string.realm_browser_version), BuildConfig.VERSION_NAME));

        // Presenter
        attachPresenter((BrowserContract.Presenter) getLastCustomNonConfigurationInstance());
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRAS_DISPLAY_MODE)) {
            presenter.requestForContentUpdate(this, this.dynamicRealm, getIntent().getExtras().getInt(EXTRAS_DISPLAY_MODE), getCallingActivity() != null);
        }

        // TODO: Reenable button when item creation works
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        if (dynamicRealm != null) {
            dynamicRealm.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_browseractivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onShowMenuSelected();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_info) {
            presenter.onInformationSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region Private
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
    //endregion

    //region RealmBrowserAdapter.Listener
    @Override
    public void onRowClicked(@NonNull DynamicRealmObject realmObject) {
        presenter.onRowSelected(realmObject);
    }
    //endregion

    //region CompoundButton.OnCheckedChangeListener
    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        presenter.onFieldSelectionChanged((int) buttonView.getTag(), isChecked);
    }
    //endregion

    //region NavigationView.OnNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.realm_browser_action_about) {
            presenter.onAboutSelected();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_wrapping) {
            presenter.onWrapTextOptionToggled();
            return true;
        }
        return false;
    }
    //endregion

    //region ViewInput
    @Override
    public void attachPresenter(@Nullable BrowserContract.Presenter presenter) {
        this.presenter = presenter;
        if (this.presenter == null) {
            this.presenter = new BrowserPresenter();
        }
        this.presenter.attachView(this);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects) {
        mAdapter.setRealmList(objects);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateWithFABVisibility(boolean visible) {
        this.fab.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateWithTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void updateWithTextWrap(boolean wrapText) {
        wrapTextSwitch.setChecked(wrapText);
        mAdapter.setShouldWrapText(wrapText);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices) {
        Menu menu = ((NavigationView) findViewById(R.id.realm_browser_navigationView)).getMenu();

        menu.findItem(R.id.realm_browser_menu_fields).getSubMenu().clear();
        checkBoxes = new AppCompatCheckBox[fields.size()];
        SubMenu subMenu = menu.findItem(R.id.realm_browser_menu_fields).getSubMenu();
        for (int i = 0; i < fields.size(); i++) {
            MenuItem m = subMenu.add(fields.get(i).getName());
            AppCompatCheckBox cb = new AppCompatCheckBox(this);
            cb.setOnCheckedChangeListener(this);
            cb.setTag(i);
            checkBoxes[i] = cb;
            m.setActionView(cb);
        }

        List<Field> selectedFieldList = new ArrayList<>();
        for (int i : selectedFieldIndices) {
            selectedFieldList.add(fields.get(i));
            checkBoxes[i].setOnCheckedChangeListener(null);
            checkBoxes[i].setChecked(true);
            checkBoxes[i].setOnCheckedChangeListener(this);
        }

        updateColumnTitle(selectedFieldList);
        mAdapter.setFieldList(selectedFieldList);
        mAdapter.notifyDataSetChanged();

        if (selectedFieldIndices.length >= 3) {
            disableCheckBoxes();
        } else {
            enableCheckboxes();
        }
    }

    @Override
    public void showInformation(long numberOfRows) {
        Toast.makeText(this, String.format(Locale.getDefault(), "Number of rows: %d", numberOfRows), Toast.LENGTH_SHORT).show();
    }
    //endregion
}