package de.jonasrottmann.realmbrowser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.realm.DynamicRealm;

public class RealmBrowserTest {

    private DynamicRealm dynamicRealm;

    @Rule
    public final TestRealmConfigurationFactory configFactory = new TestRealmConfigurationFactory();

    @Before
    public void setUp() {
        dynamicRealm = DynamicRealm.getInstance(configFactory.createConfiguration());
    }

    @After
    public void tearDown() {
        dynamicRealm.close();
    }

    @Test
    public void test() {
        // TODO
    }
}