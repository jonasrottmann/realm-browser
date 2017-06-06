package de.jonasrottmann.realmbrowser.object.view;

import android.support.annotation.NonNull;

import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;

public interface RealmBrowserViewRealmListListener {
    void onListClicked(@NonNull FieldViewPojo pojo);
}
