package de.jonasrottmann.realmsample;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class Application extends android.app.Application {

    public static final String REALM_FILE_NAME = "db10.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(REALM_FILE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        RealmBrowser.showRealmModelsNotification(this, config);
    }
}