package de.jonasrottmann.realmbrowser;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import de.jonasrottmann.realmbrowser.utils.RealmHolder;
import io.realm.RealmConfiguration;

public final class RealmBrowser {

    public static final int NOTIFICATION_ID = 1000;

    private static final RealmBrowser sInstance = new RealmBrowser();


    public static RealmBrowser getInstance() {
        return sInstance;
    }


    public static void startRealmFilesActivity(@NonNull Context context) {
        context.startActivity(RealmFilesActivity.getIntent(context));
    }


    public static void startRealmModelsActivity(@NonNull Context context, @NonNull String realmFileName) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(realmFileName)
                .build();
        startRealmModelsActivity(context, config);
    }


    public static void startRealmModelsActivity(@NonNull Context context, @NonNull RealmConfiguration realmConfiguration) {
        RealmHolder.getInstance().setRealmConfiguration(realmConfiguration);
        context.startActivity(RealmModelsActivity.getIntent(context));
    }


    public static void showRealmFilesNotification(@NonNull Context context) {
        showNotification(context, RealmFilesActivity.class);
    }


    private static void showNotification(@NonNull Context context, @NonNull Class activityClass) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.realm_browser_ic_rb)
                .setContentTitle(context.getString(R.string.realm_browser_title))
                .setContentText(context.getString(R.string.realm_browser_click_to_launch))
                .setAutoCancel(false);
        Intent notifyIntent = new Intent(context, activityClass);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
