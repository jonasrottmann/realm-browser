package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmObject;

public final class RealmBrowser {

    private static final RealmBrowser sInstance = new RealmBrowser();
    private final List<Class<? extends RealmObject>> mRealmModelList;

    private RealmBrowser() {
        mRealmModelList = new ArrayList<>();
    }

    public static RealmBrowser getInstance() {
        return sInstance;
    }


    public static void startRealmFilesActivity(@NonNull Context context) {

    }


    public static void startRealmModelsActivity(@NonNull Context context, @NonNull String realmFileName) {

    }


    public static void showRealmFilesNotification(@NonNull Context context) {

    }


    private static void showRealmNotification(@NonNull Context context, @NonNull Class activityClass) {

    }


    public List<Class<? extends RealmObject>> getRealmModelList() {
        return mRealmModelList;
    }


    @SafeVarargs
    public final void addRealmModel(Class<? extends RealmObject>... arr) {

    }
}
