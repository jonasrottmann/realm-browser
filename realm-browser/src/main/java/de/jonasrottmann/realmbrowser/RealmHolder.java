package de.jonasrottmann.realmbrowser;

import java.lang.reflect.Field;

import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;

class RealmHolder {

    private static final RealmHolder sInstance = new RealmHolder();
    private DynamicRealmObject mObject;
    private Field mField;
    private RealmConfiguration mRealmConfig;


    public static RealmHolder getInstance() {
        return sInstance;
    }


    /*
    Object
    */
    public DynamicRealmObject getObject() {
        return mObject;
    }


    public void setObject(DynamicRealmObject object) {
        mObject = object;
    }

    /*
     Field
    */
    public Field getField() {
        return mField;
    }


    public void setField(Field field) {
        mField = field;
    }

    /*
     Realm Config
    */
    public RealmConfiguration getRealmConfiguration() {
        return mRealmConfig;
    }


    public void setRealmConfiguration(RealmConfiguration realmConfig) {
        mRealmConfig = realmConfig;
    }
}
