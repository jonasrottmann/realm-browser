package de.jonasrottmann.realmbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import de.jonasrottmann.realmbrowser.utils.RealmHolder;
import io.realm.Realm;
import io.realm.RealmModel;

public class RealmModelsActivity extends AppCompatActivity {

    private Realm mRealm;
    private ArrayList<Class<? extends RealmModel>> mRealmModelClasses;
    private int mSortMode;
    private Adapter mAdapter;

    private static final int ASC = 0;
    private static final int DESC = 1;

    public static Intent getIntent(@NonNull Activity activity) {
        Intent intent = new Intent(activity, RealmModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }


    public static Intent getIntent(@NonNull Context context) {
        Intent intent = new Intent(context, RealmModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.realm_browser_ac_realm_list_view);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        mRealm = Realm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        mRealmModelClasses = new ArrayList<>(mRealm.getConfiguration().getRealmObjectClasses());
        mSortMode = ASC;
        Collections.sort(mRealmModelClasses, new Comparator<Class>() {
            @Override
            public int compare(Class c1, Class c2) {
                return c1.getSimpleName().compareTo(c2.getSimpleName());
            }
        });

        mAdapter = new Adapter(this, android.R.layout.simple_list_item_2, mRealmModelClasses, mRealm);
        ListView listView = (ListView) findViewById(R.id.realm_browser_listView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(mAdapter.getItem(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.realm_browser_menu_modelsactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.realm_browser_action_sort) {
            mSortMode = (mSortMode + 1) % 2;
            Collections.reverse(mRealmModelClasses);
            if (mSortMode == ASC) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_ascending_white_24dp));
            } else if (mSortMode == DESC) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_descending_white_24dp));
            }
            mAdapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }
        super.onDestroy();
    }


    private void onItemClicked(Class<? extends RealmModel> realmModel) {
        RealmBrowserActivity.start(this, realmModel);
    }


    private static class Adapter extends ArrayAdapter<Class<? extends RealmModel>> {

        private int mResource;
        private Realm mRealm;


        public Adapter(Context context, int res, ArrayList<Class<? extends RealmModel>> classes, Realm realm) {
            super(context, res, classes);
            mResource = res;
            mRealm = realm;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Class realmModel = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);

            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            TextView count = (TextView) convertView.findViewById(android.R.id.text2);

            title.setText(realmModel.getSimpleName());
            count.setText(String.format(Locale.US, "%d %s", mRealm.where(realmModel).findAll().size(), "rows"));

            return convertView;
        }
    }
}
