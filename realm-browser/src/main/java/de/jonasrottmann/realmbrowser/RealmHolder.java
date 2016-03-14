package de.jonasrottmann.realmbrowser;

import java.lang.reflect.Field;

import io.realm.DynamicRealmObject;

class RealmHolder {

    private static final RealmHolder sInstance = new RealmHolder();
    private DynamicRealmObject mObject;
    private Field mField;


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
}
