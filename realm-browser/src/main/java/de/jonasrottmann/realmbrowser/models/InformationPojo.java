package de.jonasrottmann.realmbrowser.models;

import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class InformationPojo {
    private final long sizeInByte;
    private final String path;

    public InformationPojo(long sizeInByte, String path) {
        this.sizeInByte = sizeInByte;
        this.path = path;
    }

    public long getSizeInByte() {
        return sizeInByte;
    }

    public String getPath() {
        return path;
    }
}
