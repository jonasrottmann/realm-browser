package de.jonasrottmann.realmsample.data;

import io.realm.RealmObject;

public class Image extends RealmObject {
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
