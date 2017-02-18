package de.jonasrottmann.realmbrowser;

import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;
import java.lang.reflect.Field;

class RealmHolder {

    private static final RealmHolder instance = new RealmHolder();
    private DynamicRealmObject object;
    private Field field;
    private RealmConfiguration realmConfiguration;


    static RealmHolder getInstance() {
        return instance;
    }


    /*
    Object
    */
    public DynamicRealmObject getObject() {
        return object;
    }


    public void setObject(DynamicRealmObject object) {
        this.object = object;
    }

    /*
     Field
    */
    public Field getField() {
        return field;
    }


    public void setField(Field field) {
        this.field = field;
    }

    /*
     Realm Config
    */
    RealmConfiguration getRealmConfiguration() {
        return realmConfiguration;
    }


    void setRealmConfiguration(RealmConfiguration realmConfig) {
        realmConfiguration = realmConfig;
    }
}
