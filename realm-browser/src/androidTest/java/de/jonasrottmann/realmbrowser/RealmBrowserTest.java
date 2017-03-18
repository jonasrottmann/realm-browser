package de.jonasrottmann.realmbrowser;

import de.jonasrottmann.realmbrowser.helper.TestRealmConfigurationFactory;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RealmBrowserTest {

    private RealmConfiguration defaultConfig;
    private DynamicRealm dynamicRealm;

    @Rule
    public final TestRealmConfigurationFactory configFactory = new TestRealmConfigurationFactory();

    @Before
    public void setUp() {
        defaultConfig = configFactory.createConfiguration();
        {
            // Needed as of https://github.com/realm/realm-java/issues/2623#issuecomment-212250150
            Realm realm = Realm.getInstance(defaultConfig);
            realm.close();
        }
        dynamicRealm = DynamicRealm.getInstance(defaultConfig);
    }

    @After
    public void tearDown() {
        if (dynamicRealm != null) {
            dynamicRealm.close();
        }
    }

    @Test
    public void test() {
        // TODO
    }
}