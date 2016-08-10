package de.jonasrottmann.realmsample;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class Application extends android.app.Application {

    public static final String REALM_FILE_NAME = "db10.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
