package de.jonasrottmann.realmsample.data;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


@RealmClass
public class User implements RealmModel {

    @PrimaryKey
    private String uuid;
    private int age;
    private boolean isBlocked;
    private String name;
    private Address address;
    private RealmList<RealmString> emailList;
    private RealmList<Contact> contactList;
    private Date creationDate;
    private byte[] byteArray;


    public void setEmailList(RealmList<RealmString> emailList) {
        this.emailList = emailList;
    }


    public void setAddress(Address address) {
        this.address = address;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }


    public void setContactList(RealmList<Contact> contactList) {
        this.contactList = contactList;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }


    public Date getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
