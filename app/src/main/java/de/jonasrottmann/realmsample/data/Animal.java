package de.jonasrottmann.realmsample.data;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
@RealmClass
public class Animal implements RealmModel {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
