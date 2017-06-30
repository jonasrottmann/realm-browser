package de.jonasrottmann.realmbrowser.browsernew;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import de.jonasrottmann.realmbrowser.helper.AbsentLiveData;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.helper.LiveDynamicRealmData;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmResults;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_CLASS;

public class BrowserViewModel extends ViewModel {

    private final MutableLiveData<DynamicRealm> realm = new MutableLiveData<>();
    private LiveData<RealmResults<DynamicRealmObject>> data;

    public BrowserViewModel() {
        data = Transformations.switchMap(realm, new Function<DynamicRealm, LiveData<RealmResults<DynamicRealmObject>>>() {
            @Override
            public LiveData<RealmResults<DynamicRealmObject>> apply(DynamicRealm input) {
                if (input == null) {
                    //noinspection unchecked
                    return AbsentLiveData.create();
                } else {
                    return new LiveDynamicRealmData(input.where(((Class<? extends RealmModel>) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_CLASS)).getSimpleName()).findAll());
                }
            }
        });
    }

    public void setRealm(DynamicRealm realm) {
        this.realm.setValue(realm);
    }

    public LiveData<RealmResults<DynamicRealmObject>> getData() {
        return data;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (realm.getValue() != null && !realm.getValue().isClosed()) realm.getValue().close();
    }
}
