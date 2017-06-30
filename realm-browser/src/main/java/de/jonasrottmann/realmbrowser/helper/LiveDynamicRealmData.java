package de.jonasrottmann.realmbrowser.helper;

import android.arch.lifecycle.LiveData;

import io.realm.DynamicRealmObject;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class LiveDynamicRealmData extends LiveData<RealmResults<DynamicRealmObject>> {

    private final RealmChangeListener<RealmResults<DynamicRealmObject>> listener = new RealmChangeListener<RealmResults<DynamicRealmObject>>() {
        @Override
        public void onChange(RealmResults<DynamicRealmObject> results) {
            setValue(results);
        }
    };
    private RealmResults<DynamicRealmObject> results;

    public LiveDynamicRealmData(RealmResults<DynamicRealmObject> realmResults) {
        results = realmResults;
    }

    @Override
    protected void onActive() {
        results.addChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        results.removeChangeListener(listener);
    }

}