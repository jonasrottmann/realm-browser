package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by Jonas Rottmann on 15/03/16.
 */
public class RealmObjectAddEditActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_FILE_NAME = "EXTRAS_REALM_FILE_NAME";
    private static final String EXTRAS_REALM_MODEL_INDEX = "REALM_MODEL_INDEX";
    private Class<? extends RealmObject> mRealmObjectClass;
    private ArrayList<Field> mFieldsList;

    public static void start(Context context, int realmModelIndex, String realmFileName) {
        Intent intent = new Intent(context, RealmObjectAddEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRAS_REALM_MODEL_INDEX, realmModelIndex);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_addedit);

        int index = getIntent().getIntExtra(EXTRAS_REALM_MODEL_INDEX, 0);
        mRealmObjectClass = RealmBrowser.getInstance().getRealmModelList().get(index);

        mFieldsList = new ArrayList<>();
        for (int i = 0; i < mRealmObjectClass.getDeclaredFields().length; i++) {
            Field f = mRealmObjectClass.getDeclaredFields()[i];
            if (!(Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))) // Ignore constant static fields
                mFieldsList.add(f);
        }

        // Init Views
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.realm_browser_addedit_linearLayout);
        for (Field field : mFieldsList) {
            // TODO inflate layout
            RealmAddEditFieldView addEditFieldView = new RealmAddEditFieldView(this);
            addEditFieldView.setField(field);
            linearLayout.addView(addEditFieldView);
        }

        // Init Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(String.format("New %s", mRealmObjectClass.getSimpleName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_addedit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_save) {
            // TODO
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
