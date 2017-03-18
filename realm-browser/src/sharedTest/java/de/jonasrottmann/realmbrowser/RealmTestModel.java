package de.jonasrottmann.realmbrowser;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import java.util.Date;
import java.util.List;

public class RealmTestModel extends RealmObject {
    // Numbers
    int anInteger;
    Integer aBoxedInteger;
    long aLong;
    Long aBoxedLong;
    short aShort;
    Short aBoxedShort;
    byte aByte;
    Byte aBoxedByte;
    double aDouble;
    Double aBoxedDouble;
    float aFloat;
    Float aBoxedFloat;
    // Bool
    boolean aBool;
    Boolean aBoxedBool;
    // String
    String aString;
    // Blob
    byte[] aBlob;
    // Blob
    Date aDate;
    // Parametrized
    @Ignore
    List<String> aStringList;
    RealmList<RealmTestModel> aRealmList;
    // Object
    RealmTestModel anObject;

    public RealmTestModel() {

    }
}