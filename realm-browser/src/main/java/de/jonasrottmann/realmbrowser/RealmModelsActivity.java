package de.jonasrottmann.realmbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

public class RealmModelsActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_FILE_NAME = "EXTRAS_REALM_FILE_NAME";

    private Realm mRealm;
    private ArrayList<Class<? extends RealmModel>> mRealmModelClasses;



    public static Intent getIntent(@NonNull Activity activity, @NonNull String realmFileName) {
        Intent intent = new Intent(activity, RealmModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        return intent;
    }



    public static Intent getIntent(@NonNull Context context, @NonNull String realmFileName) {
        Intent intent = new Intent(context, RealmModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.realm_browser_ac_realm_list_view);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);

        RealmConfiguration config = new RealmConfiguration.Builder(this).name(realmFileName).build();
        mRealm = Realm.getInstance(config);
        mRealmModelClasses = new ArrayList<>(mRealm.getConfiguration().getRealmObjectClasses());

        final Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_2, mRealmModelClasses, mRealm);
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
    protected void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }
        super.onDestroy();
    }



    private void onItemClicked(Class<? extends RealmModel> realmModel) {
        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);
        RealmBrowserActivity.start(this, realmModel, realmFileName);
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
