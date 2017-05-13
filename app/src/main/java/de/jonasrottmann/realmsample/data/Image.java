package de.jonasrottmann.realmsample.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Image extends RealmObject {
    @PrimaryKey
    private int id;
    private byte[] bytes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
