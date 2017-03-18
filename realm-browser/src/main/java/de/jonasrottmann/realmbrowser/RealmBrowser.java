package de.jonasrottmann.realmbrowser;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import de.jonasrottmann.realmbrowser.files.view.FilesActivity;
import de.jonasrottmann.realmbrowser.helper.RealmHolder;
import de.jonasrottmann.realmbrowser.models.view.ModelsActivity;
import io.realm.RealmConfiguration;
import java.util.Collections;

public final class RealmBrowser {

    private static final int NOTIFICATION_ID = 1000;

    /**
     * @param context A valid {@link Context}
     */
    @SuppressWarnings("WeakerAccess")
    public static void startRealmFilesActivity(@NonNull Context context) {
        context.startActivity(FilesActivity.getIntent(context));
    }

    /**
     * @param context A valid {@link Context}
     * @param realmFileName The name of the realm file to open.
     */
    @SuppressWarnings("WeakerAccess")
    public static void startRealmModelsActivity(@NonNull Context context, @NonNull String realmFileName) {
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmFileName).build();
        startRealmModelsActivity(context, config);
    }

    /**
     * @param context A valid {@link Context}
     * @param realmConfiguration The config of the realm to open.
     */
    @SuppressWarnings("WeakerAccess")
    public static void startRealmModelsActivity(@NonNull Context context, @NonNull RealmConfiguration realmConfiguration) {
        RealmHolder.getInstance().setRealmConfiguration(realmConfiguration);
        context.startActivity(ModelsActivity.getIntent(context));
    }

    /**
     * @param context A valid {@link Context}
     */
    @SuppressWarnings("WeakerAccess")
    public static void showRealmFilesNotification(@NonNull Context context) {
        Intent notifyIntent = new Intent(context, FilesActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        showNotification(context, notifyPendingIntent);
    }

    /**
     * @param context A valid {@link Context}
     * @param realmConfiguration The config of the realm to open.
     */
    @SuppressWarnings("WeakerAccess")
    public static void showRealmModelsNotification(@NonNull Context context, @NonNull RealmConfiguration realmConfiguration) {
        RealmHolder.getInstance().setRealmConfiguration(realmConfiguration);
        Intent notifyIntent = ModelsActivity.getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        showNotification(context, pendingIntent);
    }

    private static void showNotification(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.realm_browser_ic_rb)
            .setContentTitle(context.getString(R.string.realm_browser_title))
            .setContentText(context.getApplicationContext().getPackageName())
            .setAutoCancel(false);

        builder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * @param context A valid {@link Context}
     * @return The id of the added shortcut (<code>null</code> if this feature is not supported on the device). Is used if you want to remove this shortcut later on.
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static String addFilesShortcut(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            final String id = "realm_browser_ac_files";
            final ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            final ShortcutInfo shortcut = new ShortcutInfo.Builder(context, id).setShortLabel("Files")
                .setLongLabel("Open realm-browser files activity")
                .setIcon(Icon.createWithResource(context, R.drawable.realm_browser_shortcut_rb))
                .setIntent(FilesActivity.getIntent(context).setAction(Intent.ACTION_VIEW))
                .build();
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
            return id;
        } else {
            return null;
        }
    }

    /**
     * @param context A valid {@link Context}
     * @param realmConfiguration The config of the realm to open.
     * @return The id of the added shortcut (<code>null</code> if this feature is not supported on the device). Is used if you want to remove this shortcut later on.
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static String addModelsShortcut(@NonNull Context context, @NonNull RealmConfiguration realmConfiguration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            RealmHolder.getInstance().setRealmConfiguration(realmConfiguration);
            final String id = "realm_browser_ac_models";
            final ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            final ShortcutInfo shortcut = new ShortcutInfo.Builder(context, id).setShortLabel("Models")
                .setLongLabel("Open realm-browser models activity")
                .setIcon(Icon.createWithResource(context, R.drawable.realm_browser_shortcut_rb))
                .setIntents(new Intent[] {
                    FilesActivity.getIntent(context).setAction(Intent.ACTION_VIEW), ModelsActivity.getIntent(context).setAction(Intent.ACTION_VIEW)
                })
                .build();
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
            return id;
        } else {
            return null;
        }
    }
}
