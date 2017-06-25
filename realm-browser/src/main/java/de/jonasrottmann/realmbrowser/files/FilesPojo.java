package de.jonasrottmann.realmbrowser.files;

import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesPojo {
    private final String name;
    private final String size;
    private final long sizeInByte;

    FilesPojo(String name, String size, long sizeInByte) {
        this.name = name;
        this.size = size;
        this.sizeInByte = sizeInByte;
    }

    String getName() {
        return name;
    }

    String getSize() {
        return size;
    }

    long getSizeInByte() {
        return sizeInByte;
    }
}
