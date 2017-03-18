package de.jonasrottmann.realmbrowser.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractorImpl;
import de.jonasrottmann.realmbrowser.helper.RealmHolder;
import de.jonasrottmann.realmbrowser.models.model.ModelPojo;
import io.realm.Realm;
import io.realm.RealmModel;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsInteractor extends BaseInteractorImpl<ModelsContract.Presenter> implements ModelsContract.Interactor {
    @ModelsContract.SortMode
    private int sortMode = ModelsContract.SortMode.ASC;
    private String filter;

    ModelsInteractor(ModelsContract.Presenter presenter) {
        super(presenter);
    }

    //region InteractorInput
    @Override
    public void requestForContentUpdate() {
        getPresenter().updateWithModels(sortPojos(filterPojos(getAllModelPojos(), filter), sortMode), sortMode);
    }

    @Override
    public void updateWithFilter(String filter) {
        this.filter = filter;
        requestForContentUpdate();
    }

    @Override
    public void updateWithSortModeChanged() {
        //noinspection WrongConstant
        this.sortMode = (this.sortMode + 1) % 2;
        requestForContentUpdate();
    }
    //endregion

    //region Helper
    @NonNull
    private static ArrayList<ModelPojo> getAllModelPojos() {
        Realm realm = Realm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        ArrayList<Class<? extends RealmModel>> realmModelClasses = new ArrayList<>(realm.getConfiguration().getRealmObjectClasses());
        ArrayList<ModelPojo> pojos = new ArrayList<>();
        for (Class<? extends RealmModel> klass : realmModelClasses) {
            pojos.add(new ModelPojo(klass, realm.where(klass).findAll().size()));
        }
        realm.close();
        return pojos;
    }

    @NonNull
    private static ArrayList<ModelPojo> sortPojos(@NonNull ArrayList<ModelPojo> pojos, @ModelsContract.SortMode int sortMode) {
        Collections.sort(pojos, new Comparator<ModelPojo>() {
            @Override
            public int compare(ModelPojo o1, ModelPojo o2) {
                return o1.getKlass().getSimpleName().compareTo(o2.getKlass().getSimpleName());
            }
        });
        if (sortMode == ModelsContract.SortMode.ASC) {
            Collections.reverse(pojos);
        }
        return pojos;
    }

    @NonNull
    private static ArrayList<ModelPojo> filterPojos(@NonNull ArrayList<ModelPojo> pojos, @Nullable String filter) {
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

    //endregion
}
