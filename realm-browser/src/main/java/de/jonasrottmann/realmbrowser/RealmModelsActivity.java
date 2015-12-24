package de.jonasrottmann.realmbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

import static de.jonasrottmann.realmbrowser.RealmBrowser.getInstance;

public class RealmModelsActivity extends AppCompatActivity {

    private static final String EXTRAS_REALM_FILE_NAME = "EXTRAS_REALM_FILE_NAME";

    private Realm mRealm;

    public static void start(@NonNull Activity activity, @NonNull String realmFileName) {
        Intent intent = new Intent(activity, RealmModelsActivity.class);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        activity.startActivity(intent);
    }



    public static void start(@NonNull Context context, @NonNull String realmFileName) {
        Intent intent = new Intent(context, RealmModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRAS_REALM_FILE_NAME, realmFileName);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_list_view);

        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);

        RealmConfiguration config = new RealmConfiguration.Builder(this).name(realmFileName).build();
        mRealm = Realm.getInstance(config);

        List<ModuleWithCount> list = new ArrayList<>();
        for (Class<? extends RealmObject> file : getInstance().getRealmModelList()) {
            ModuleWithCount moduleWithCount = new ModuleWithCount(file.getSimpleName(), mRealm.where(file).count());
            list.add(moduleWithCount);
        }


        ModuleWithCountAdapter adapter = new ModuleWithCountAdapter(this, R.layout.realm_browser_item_realm_module, list);
        ListView listView = (ListView) findViewById(R.id.realm_browser_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onItemClicked(position);
                    }
                });
    }


    private void onItemClicked(int position) {
        String realmFileName = getIntent().getStringExtra(EXTRAS_REALM_FILE_NAME);
        RealmBrowserActivity.start(this, position, realmFileName);
    }

    private static class ModuleWithCount {
        public final String name;
        public final long count;

        public ModuleWithCount(String name, long count) {
            this.name = name;
            this.count = count;
        }
    }

    private static class ModuleWithCountAdapter extends ArrayAdapter<ModuleWithCount> {

        private int mResource;

        public ModuleWithCountAdapter(Context context, int res, List<ModuleWithCount> modulesWithCount) {
            super(context, res, modulesWithCount);
            mResource = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModuleWithCount moduleWithCount = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);

            TextView title = (TextView) convertView.findViewById(R.id.realm_browser_title);
            TextView count = (TextView) convertView.findViewById(R.id.realm_browser_count);

            title.setText(moduleWithCount.name);
            count.setText(String.valueOf(moduleWithCount.count));

            return convertView;
        }
    }
}
