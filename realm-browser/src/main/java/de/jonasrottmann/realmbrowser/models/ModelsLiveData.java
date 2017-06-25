package de.jonasrottmann.realmbrowser.models;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsLiveData extends LiveData<List<ModelPojo>> {
    private final Realm realm;
    @ModelsViewModel.SortMode
    private int sortMode;
    private String filter;
    private List<ModelPojo> unfiltered;

    ModelsLiveData(Realm realm, int sortMode, @NonNull String filter) {
        this.realm = realm;
        this.sortMode = sortMode;
        this.filter = filter;
        loadModels();
    }

    @NonNull
    private static List<ModelPojo> sortPojos(@NonNull List<ModelPojo> pojos, @ModelsViewModel.SortMode int sortMode) {
        Collections.sort(pojos, new Comparator<ModelPojo>() {
            @Override
            public int compare(ModelPojo o1, ModelPojo o2) {
                return o1.getKlass().getSimpleName().compareTo(o2.getKlass().getSimpleName());
            }
        });
        if (sortMode == ModelsViewModel.SortMode.DESC) {
            Collections.reverse(pojos);
        }
        return pojos;
    }

    @NonNull
    private static List<ModelPojo> filterPojos(@NonNull List<ModelPojo> pojos, @Nullable String filter) {
        ArrayList<ModelPojo> filteredPojos = null;
        if (filter != null && !filter.isEmpty()) {
            filteredPojos = new ArrayList<>();
            for (ModelPojo pojo : pojos) {
                if (pojo.getKlass().getSimpleName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredPojos.add(pojo);
                }
            }
        }
        return filteredPojos == null ? pojos : filteredPojos;
    }

    void onSortModeChanged(@ModelsViewModel.SortMode int sortMode) {
        this.sortMode = sortMode;
        setValue(filterPojos(sortPojos(unfiltered, this.sortMode), this.filter));
    }

    void onFilterChanged(@Nullable String filter) {
        this.filter = filter;
        setValue(filterPojos(sortPojos(unfiltered, this.sortMode), this.filter));
    }

    int getSortMode() {
        return sortMode;
    }

    String getFilter() {
        return filter;
    }

    void refreshData() {
        loadModels();
    }

    private void loadModels() {
        ArrayList<Class<? extends RealmModel>> realmModelClasses = new ArrayList<>(realm.getConfiguration().getRealmObjectClasses());
        ArrayList<ModelPojo> pojos = new ArrayList<>();
        for (Class<? extends RealmModel> klass : realmModelClasses) {
            pojos.add(new ModelPojo(klass, realm.where(klass).findAll().size()));
        }
        this.unfiltered = pojos;
        setValue(filterPojos(sortPojos(unfiltered, this.sortMode), this.filter));
    }
}
