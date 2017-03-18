package de.jonasrottmann.realmbrowser.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RealmPreferences {

    private static final String PREF_NAME = "pref.realm";
    private static final String WRAP_TEXT = "WRAP_TEXT";
    private final Context context;


    public RealmPreferences(Context context) {
        this.context = context;
    }


    private SharedPreferences preferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public void setShouldWrapText(boolean value) {
        preferences().edit().putBoolean(WRAP_TEXT, value).apply();
    }


    public boolean shouldWrapText() {
        return preferences().getBoolean(WRAP_TEXT, false);
    }
}
