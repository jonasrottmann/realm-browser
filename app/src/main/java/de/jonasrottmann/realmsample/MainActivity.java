package de.jonasrottmann.realmsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import de.jonasrottmann.realmsample.data.Address;
import de.jonasrottmann.realmsample.data.Contact;
import de.jonasrottmann.realmsample.data.RealmString;
import de.jonasrottmann.realmsample.data.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String REALM_FILE_NAME = "db10.realm";
    private TextView mTxtTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        findViewById(R.id.btnInsert).setOnClickListener(this);
        findViewById(R.id.btnRemove).setOnClickListener(this);
        findViewById(R.id.btnOpenFile).setOnClickListener(this);
        findViewById(R.id.btnOpenModel).setOnClickListener(this);

        updateTitle();

        RealmBrowser.showRealmFilesNotification(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInsert:
                insertUsers(100);
                updateTitle();
                break;
            case R.id.btnRemove:
                removeAllUsers();
                updateTitle();
                break;
            case R.id.btnOpenFile:
                startRealmFilesActivity();
                break;
            case R.id.btnOpenModel:
                startRealmModelsActivity();
                break;
        }
    }



    private void updateTitle() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .build();
        Realm realm = Realm.getInstance(config);
        int size = realm.where(User.class).findAll().size();
        mTxtTitle.setText(String.format("Items in database: %d", size));
        realm.close();
    }



    private void removeAllUsers() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .build();
        Realm realm = Realm.getInstance(config);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(User.class).findAll();
            }
        });

        realm.close();
    }



    private void insertUsers(int count) {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .build();
        Realm realm = Realm.getInstance(config);

        final List<User> userList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Address address = new Address();
            address.setLat(49.8397473);
            address.setLon(24.0233077);

            User user = new User();
            user.setName("Jon Doe " + i);
            user.setIsBlocked(Math.random() > 0.5);
            user.setAge(i);
            user.setAddress(address);

            RealmList<RealmString> emailList = new RealmList<>();
            for (int k = 0; k < 5; k++) {
                emailList.add(new RealmString("jondoe" + k + "@gmail.com"));
            }
            user.setEmailList(emailList);

            RealmList<Contact> contactList = new RealmList<>();
            for (int k = 0; k < 10; k++) {
                Contact contact = new Contact();
                contact.setId(k);
                contact.setName("Filip");
                contactList.add(contact);
            }
            user.setContactList(contactList);

            userList.add(user);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(userList);
            }
        });

        realm.close();
    }



    private void startRealmFilesActivity() {
        RealmBrowser.startRealmFilesActivity(this);
    }



    private void startRealmModelsActivity() {
        RealmBrowser.startRealmModelsActivity(this, REALM_FILE_NAME);
    }
}
