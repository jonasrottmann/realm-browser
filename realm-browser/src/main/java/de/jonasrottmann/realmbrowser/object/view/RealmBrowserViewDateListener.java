package de.jonasrottmann.realmbrowser.object.view;

import android.support.annotation.NonNull;

import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;

interface RealmBrowserViewDateListener {
    void onInfoClicked();

    void onOpenDatePickerClicked(@NonNull FieldViewPojo pojo);
}