package de.jonasrottmann.realmbrowser.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class AllFieldTypesModel extends RealmObject {
    @Ignore public String field_Ignored;
    @Index public String field_String;
    public short field_short;
    public Short field_Short;
    public int field_int;
    public Integer field_Integer;
    @PrimaryKey public long field_long;
    public Long field_Long;
    public byte field_byte;
    public Byte field_Byte;
    public float field_float;
    public Float field_Float;
    public double field_double;
    public Double field_Double;
    public boolean field_boolean;
    public Boolean field_Boolean;
    public Date field_Date;
    public byte[] field_Binary;
    public AllFieldTypesModel field_Object;
    public RealmList<AllFieldTypesModel> field_List;
}