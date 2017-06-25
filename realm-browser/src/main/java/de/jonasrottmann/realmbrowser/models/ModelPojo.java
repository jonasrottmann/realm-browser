package de.jonasrottmann.realmbrowser.models;

import android.support.annotation.RestrictTo;

import io.realm.RealmModel;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ModelPojo {
    final Class<? extends RealmModel> klass;
    long count;

    public ModelPojo(Class<? extends RealmModel> klass, long count) {
        this.klass = klass;
        this.count = count;
    }

    public Class<? extends RealmModel> getKlass() {
        return klass;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
