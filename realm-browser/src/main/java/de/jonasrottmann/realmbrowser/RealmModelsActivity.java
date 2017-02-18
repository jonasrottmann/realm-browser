package de.jonasrottmann.realmbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import io.realm.Realm;
import io.realm.RealmModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RealmModelsActivity extends AppCompatActivity {

    private static final int ASC = 0;
    private static final int DESC = 1;
    private Realm realm;
    private ArrayList<Class<? extends RealmModel>> realmModelClasses;
    private int sortMode;
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_realm);

        realm = Realm.getInstance(RealmHolder.getInstance().getRealmConfiguration());
        realmModelClasses = new ArrayList<>(realm.getConfiguration().getRealmObjectClasses());
        sortMode = ASC;
        Collections.sort(realmModelClasses, new Comparator<Class>() {
            @Override
            public int compare(Class c1, Class c2) {
                return c1.getSimpleName().compareTo(c2.getSimpleName());
            }
        });

        adapter = new Adapter(this, android.R.layout.simple_list_item_2, realmModelClasses, realm);
        ListView listView = (ListView) findViewById(R.id.realm_browser_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(adapter.getItem(position));
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
            sortMode = (sortMode + 1) % 2;
            Collections.reverse(realmModelClasses);
            if (sortMode == ASC) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_ascending_white_24dp));
            } else if (sortMode == DESC) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_descending_white_24dp));
            }
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        if (realm != null) {
            realm.close();
        }
        super.onDestroy();
    }


    private void onItemClicked(Class<? extends RealmModel> realmModel) {
        RealmBrowserActivity.start(this, realmModel);
    }


    private static class Adapter extends ArrayAdapter<Class<? extends RealmModel>> {

        private final int res;
        private final Realm realm;


        public Adapter(Context context, int res, ArrayList<Class<? extends RealmModel>> classes, Realm realm) {
            super(context, res, classes);
            this.realm = realm;
            this.res = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Class realmModel = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(res, parent, false);
            }

            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            TextView count = (TextView) convertView.findViewById(android.R.id.text2);

            title.setText(realmModel.getSimpleName());
            count.setText(String.format(Locale.US, "%d %s", realm.where(realmModel).findAll().size(), "rows"));

            return convertView;
        }
    }
}
