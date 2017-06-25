package de.jonasrottmann.realmbrowser.models;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.browser.BrowserContract;
import de.jonasrottmann.realmbrowser.browser.view.RealmBrowserActivity;
import de.jonasrottmann.realmbrowser.helper.DataHolder;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ModelsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LifecycleRegistryOwner {
    private static final String SEARCH_KEY = "search";
    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ModelsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MenuItem sortMenuItem;
    private SearchView searchView;
    private ModelsViewModel viewModel;

    public static Intent getIntent(@NonNull Context context) {
        Intent intent = new Intent(context, ModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_recycler);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        viewModel = ViewModelProviders.of(this).get(ModelsViewModel.class);
        viewModel.getData().observe(this, new Observer<List<ModelPojo>>() {
            @Override
            public void onChanged(@Nullable List<ModelPojo> modelPojos) {
                setSortIcon();
                adapter.swapList(modelPojos);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        setSortIcon();

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshData();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_dark_purple);

        adapter = new ModelsAdapter(new ArrayList<ModelPojo>(), new ModelsAdapter.OnModelSelectedListener() {
            @Override
            public void onModelSelected(ModelPojo file) {
                DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CLASS, file.getKlass());
                RealmBrowserActivity.start(ModelsActivity.this, BrowserContract.DisplayMode.REALM_CLASS);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.realm_browser_recycler);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.realm_browser_menu_modelsactivity, menu);
        this.sortMenuItem = menu.findItem(R.id.realm_browser_action_sort);
        MenuItem searchMenuItem = menu.findItem(R.id.realm_browser_action_filter);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (viewModel.getFilter() != null && !viewModel.getFilter().isEmpty()) {
            ((SearchView) menu.findItem(R.id.realm_browser_action_filter).getActionView()).setIconified(false);
            searchView.requestFocus();
            searchView.setQuery(viewModel.getFilter(), true);
            searchView.clearFocus();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.realm_browser_action_sort) {
            swipeRefreshLayout.setRefreshing(true);
            viewModel.changeSortMode();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_share) {
            presentShareDialog();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_info) {
            showInformation();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setSortIcon() {
        if (viewModel.getSortMode() == ModelsViewModel.SortMode.ASC && sortMenuItem != null) {
            ModelsActivity.this.sortMenuItem.setIcon(ContextCompat.getDrawable(ModelsActivity.this, R.drawable.realm_browser_ic_sort_ascending_white_24dp));
        } else if (viewModel.getSortMode() == ModelsViewModel.SortMode.DESC && sortMenuItem != null) {
            ModelsActivity.this.sortMenuItem.setIcon(ContextCompat.getDrawable(ModelsActivity.this, R.drawable.realm_browser_ic_sort_descending_white_24dp));
        }
    }

    public void presentShareDialog() {
        InformationPojo informationPojo = viewModel.getInformation();
        if (informationPojo != null) {
            Uri contentUri = FileProvider.getUriForFile(this, String.format("%s.share", this.getPackageName()), new File(informationPojo.getPath()));
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/*");
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(intentShareFile, "Share Realm File"));
        }
    }

    public void showInformation() {
        InformationPojo informationPojo = viewModel.getInformation();
        if (informationPojo != null) {
            Toast.makeText(this, String.format("%s\nSize: %s", informationPojo.getPath(), Formatter.formatShortFileSize(this, informationPojo.getSizeInByte())), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        swipeRefreshLayout.setRefreshing(true);
        viewModel.changeFilter(newText);
        return true;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
