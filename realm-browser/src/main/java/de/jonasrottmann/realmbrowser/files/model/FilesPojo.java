package de.jonasrottmann.realmbrowser.files.model;

import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FilesPojo {
    private final String name;
    private final String size;
    private final long sizeInByte;

    public FilesPojo(String name, String size, long sizeInByte) {
        this.name = name;
        this.size = size;
        this.sizeInByte = sizeInByte;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public long getSizeInByte() {
        return sizeInByte;
    }
}
