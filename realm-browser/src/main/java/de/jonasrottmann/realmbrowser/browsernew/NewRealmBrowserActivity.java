package de.jonasrottmann.realmbrowser.browsernew;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.jonasrottmann.realmbrowser.R;

public class NewRealmBrowserActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_realm_browser);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }
}
