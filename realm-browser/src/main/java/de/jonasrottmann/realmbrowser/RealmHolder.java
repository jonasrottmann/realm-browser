package de.jonasrottmann.realmbrowser;

import java.lang.reflect.Field;

import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;

class RealmHolder {

    private static final RealmHolder sInstance = new RealmHolder();
    private DynamicRealmObject mObject;
    private Field mField;
    private RealmConfiguration mRealmConfiguration;



    public static RealmHolder getInstance() {
        return sInstance;
    }



    public DynamicRealmObject getObject() {
        return mObject;
    }



    public void setObject(DynamicRealmObject object) {
        mObject = object;
    }



    public Field getField() {
        return mField;
    }



    public void setField(Field field) {
        mField = field;
    }



    public RealmConfiguration getRealmConfiguration() {
        return mRealmConfiguration;
    }



    public void setRealmConfiguration(RealmConfiguration mRealmConfiguration) {
        this.mRealmConfiguration = mRealmConfiguration;
    }
}
