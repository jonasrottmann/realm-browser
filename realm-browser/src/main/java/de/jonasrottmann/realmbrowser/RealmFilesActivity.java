package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RealmFilesActivity extends AppCompatActivity {

    private List<String> ignoreExtensionList;
    private ArrayAdapter<Pair<String, String>> adapter;


    public static Intent getIntent(@NonNull Context context) {
        Intent intent = new Intent(context, RealmFilesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_list_view);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        // Disable SwipeRefreshLayout - not used in this Activity
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);

        ignoreExtensionList = new ArrayList<>();
        ignoreExtensionList.add(".log");
        ignoreExtensionList.add(".log_a");
        ignoreExtensionList.add(".log_b");
        ignoreExtensionList.add(".lock");
        ignoreExtensionList.add(".management");

        File dataDir = new File(getApplicationInfo().dataDir, "files");
        File[] files = dataDir.listFiles();
        ArrayList<Pair<String, String>> fileList = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            if (isValid(fileName)) {
                fileList.add(Pair.create(fileName, Formatter.formatShortFileSize(this, file.length())));
            }
        }

        adapter = new Adapter(this, android.R.layout.simple_list_item_2, fileList);
        ListView listView = (ListView) findViewById(R.id.realm_browser_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(adapter.getItem(position).first);
            }
        });
    }


    private boolean isValid(String fileName) {
        boolean isValid = true;
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            String extension = fileName.substring(index);
            isValid = !ignoreExtensionList.contains(extension);
        }
        return isValid;
    }


    private void onItemClicked(@NonNull String realmFileName) {
        try {
            RealmConfiguration config = new RealmConfiguration.Builder().name(realmFileName).build();
            RealmHolder.getInstance().setRealmConfiguration(config);
            Realm realm = Realm.getInstance(config);
            realm.close();
            startActivity(RealmModelsActivity.getIntent(this));
        } catch (RealmMigrationNeededException e) {
            Toast.makeText(getApplicationContext(), "RealmMigrationNeededException", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Can't open realm instance", Toast.LENGTH_SHORT).show();
        }
    }


    private static class Adapter extends ArrayAdapter<Pair<String, String>> {

        private final int resource;


        Adapter(Context context, int res, ArrayList<Pair<String, String>> classes) {
            super(context, res, classes);
            resource = res;
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pair<String, String> filePair = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            TextView filesize = (TextView) convertView.findViewById(android.R.id.text2);

            title.setText(filePair.first);
            filesize.setText(filePair.second);

            return convertView;
        }
    }
}
