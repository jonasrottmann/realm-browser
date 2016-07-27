package de.jonasrottmann.realmbrowser;

import android.content.Context;

import io.realm.RealmConfiguration;

public final class RealmBrowser {

    private static final RealmBrowser sInstance = new RealmBrowser();


    public static RealmBrowser getInstance() {
        return sInstance;
    }


    public static void startRealmFilesActivity(Context context) {

    }

    public static void startRealmModelsActivity(Context context, String realmFileName) {

    }

    public static void startRealmModelsActivity(Context context, RealmConfiguration realmConfiguration) {

    }


    public static void showRealmFilesNotification(Context context) {

    }


    private static void showRealmNotification(Context context, Class activityClass) {

    }
}
