package de.jonasrottmann.realmbrowser;

import java.lang.reflect.Field;

import io.realm.RealmObject;

class RealmHolder {

    private static final RealmHolder sInstance = new RealmHolder();
    private RealmObject mObject;
    private Field mField;



    public static RealmHolder getInstance() {
        return sInstance;
    }



    public RealmObject getObject() {
        return mObject;
    }



    public void setObject(RealmObject object) {
        mObject = object;
    }



    public Field getField() {
        return mField;
    }



    public void setField(Field field) {
        mField = field;
    }
}
