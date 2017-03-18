package de.jonasrottmann.realmsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.jonasrottmann.realmbrowser.RealmBrowser;
import de.jonasrottmann.realmsample.data.Address;
import de.jonasrottmann.realmsample.data.Contact;
import de.jonasrottmann.realmsample.data.RealmString;
import de.jonasrottmann.realmsample.data.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInsert:
                insertUsers(100);
                updateTitle();
                break;
            case R.id.btnRemove:
                clearRealm();
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
        Realm realm = Realm.getDefaultInstance();

        int size = realm.where(User.class).findAll().size();
        mTxtTitle.setText(String.format("Items in database: %d", size));
        realm.close();
    }


    private void clearRealm() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        realm.close();
    }


    private void insertUsers(int count) {
        Realm realm = Realm.getDefaultInstance();

        final List<User> userList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Address address = new Address();
            address.setLat(49.8397473);
            address.setLon(24.0233077);

            User user = new User();
            user.setName(new RealmString("Jon Doe " + i));
            user.setIsBlocked(Math.random() > 0.5);
            user.setAge(i);
            user.setAddress(address);
            user.setUuid(UUID.randomUUID().toString());
            user.setByteArray(new byte[] { 1, 2, 3 });
            user.setCreationDate(new Date(System.currentTimeMillis()));

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
        Realm realm = Realm.getDefaultInstance();
        RealmConfiguration configuration = realm.getConfiguration();
        realm.close();
        RealmBrowser.startRealmModelsActivity(this, configuration);
    }
}
