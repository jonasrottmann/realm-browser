package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.SharedPreferences;

class RealmPreferences {

    private static final String PREF_NAME = "pref.realm";
    private static final String WRAP_TEXT = "WRAP_TEXT";
    private final Context context;


    RealmPreferences(Context context) {
        this.context = context;
    }


    private SharedPreferences preferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    void setShouldWrapText(boolean value) {
        preferences().edit().putBoolean(WRAP_TEXT, value).apply();
    }


    boolean shouldWrapText() {
        return preferences().getBoolean(WRAP_TEXT, false);
    }
}
