package de.jonasrottmann.realmbrowser.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.io.File;
import java.lang.annotation.Retention;

import de.jonasrottmann.realmbrowser.helper.DataHolder;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static de.jonasrottmann.realmbrowser.models.ModelsViewModel.SortMode.ASC;
import static de.jonasrottmann.realmbrowser.models.ModelsViewModel.SortMode.DESC;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@SuppressWarnings("WeakerAccess")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ModelsViewModel extends AndroidViewModel {

    private final Realm realm;
    private final RealmConfiguration config;
    private final ModelsLiveData data;

    public ModelsViewModel(Application application) {
        super(application);
        this.config = (RealmConfiguration) DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG);
        this.realm = Realm.getInstance(config);
        this.data = new ModelsLiveData(realm, ASC, "");
    }

    public ModelsLiveData getData() {
        return data;
    }

    public void refreshData() {
        data.refreshData();
    }

    public void changeSortMode() {
        data.onSortModeChanged((data.getSortMode() + 1) % 2);
    }

    public String getFilter() {
        return data.getFilter();
    }

    public void changeFilter(@NonNull String filter) {
        data.onFilterChanged(filter);
    }

    @Nullable
    public InformationPojo getInformation() {
        if (config == null) return null;
        File realmFile = new File(config.getPath());
        long sizeInByte = 0;
        if (realmFile.exists() && !realmFile.isDirectory()) {
            sizeInByte = realmFile.length();
        }
        return new InformationPojo(sizeInByte, realmFile.getPath());
    }

    @SortMode
    public int getSortMode() {
        return data.getSortMode();
    }

    @Override
    protected void onCleared() {
        Timber.d("ModelsViewModel.onCleared()");
        realm.close();
        super.onCleared();
    }

    @Retention(SOURCE)
    @IntDef({ASC, DESC})
    @interface SortMode {
        int ASC = 0;
        int DESC = 1;
    }
}
