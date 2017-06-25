package de.jonasrottmann.realmbrowser.files;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.models.view.ModelsActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmMigrationNeededException;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FilesActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private FilesAdapter adapter;
    private FilesViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static Intent getIntent(@NonNull Context context) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_recycler);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(FilesViewModel.class);
        viewModel.getFiles().observe(this, new Observer<List<FilesPojo>>() {
            @Override
            public void onChanged(@Nullable List<FilesPojo> filesPojos) {
                adapter.swapList(filesPojos);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Disable SwipeRefreshLayout - not used in this Activity
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_dark_purple);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshFiles();
            }
        });

        // Adapter init
        adapter = new FilesAdapter(new ArrayList<FilesPojo>(), new FilesAdapter.OnFileSelectedListener() {
            @Override
            public void onFileSelected(FilesPojo file) {
                FilesActivity.this.onFileSelected(file);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.realm_browser_recycler);
        recyclerView.setAdapter(adapter);
    }

    private void onFileSelected(FilesPojo item) {
        try {
            RealmConfiguration config = new RealmConfiguration.Builder().name(item.getName()).build();
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CONFIG, config);
            Realm realm = Realm.getInstance(config);
            realm.close();
            startActivity(ModelsActivity.getIntent(this));
        } catch (RealmMigrationNeededException e) {
            showToast(String.format("%s %s", getString(R.string.realm_browser_open_error), getString(R.string.realm_browser_error_migration)));
        } catch (RealmFileException e) {
            showToast(String.format("%s %s", getString(R.string.realm_browser_open_error), e.getMessage()));
        } catch (Exception e) {
            showToast(String.format("%s %s", getString(R.string.realm_browser_open_error), getString(R.string.realm_browser_error_openinstances)));
        }
    }

    private void showToast(@Nullable String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
