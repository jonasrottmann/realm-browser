package de.jonasrottmann.realmbrowser;

import java.lang.reflect.Field;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class UtilsTest {
    //region createParametrizedName
    @Test
    public void createParametrizedNameForListField() throws NoSuchFieldException {
        Field f = RealmModel.class.getDeclaredField("aStringList");
        assertEquals("List<String>", Utils.createParametrizedName(f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createParametrizedNameForNull() {
        //noinspection ConstantConditions
        Utils.createParametrizedName(null);
    }
    //endregion

    //region createBlobValueString
    @Test
    public void createBlobValueStringForNull() {
        assertNull(Utils.createBlobValueString(null));
    }

    @Test
    public void createBlobValueStringForEmptyBlob() {
        assertEquals("byte[] = {}", Utils.createBlobValueString(new byte[] {}));
    }

    @Test
    public void createBlobValueStringForSingleEntryBlob() {
        assertEquals("byte[] = {1}", Utils.createBlobValueString(new byte[] { 1 }));
    }

    @Test
    public void createBlobValueStringForThreeEntryBlob() {
        assertEquals("byte[] = {1, 2, 3}", Utils.createBlobValueString(new byte[] { 1, 2, 3 }));
    }
    //endregion

    @Test
    public void isInteger() throws NoSuchFieldException {
        assertTrue(Utils.isInteger(RealmModel.class.getDeclaredField("anInteger")));
        assertTrue(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isInteger(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isLong() throws NoSuchFieldException {
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertTrue(Utils.isLong(RealmModel.class.getDeclaredField("aLong")));
        assertTrue(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isLong(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isShort() throws NoSuchFieldException {
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertTrue(Utils.isShort(RealmModel.class.getDeclaredField("aShort")));
        assertTrue(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isShort(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isByte() throws NoSuchFieldException {
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertTrue(Utils.isByte(RealmModel.class.getDeclaredField("aByte")));
        assertTrue(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isByte(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isFloat() throws NoSuchFieldException {
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertTrue(Utils.isFloat(RealmModel.class.getDeclaredField("aFloat")));
        assertTrue(Utils.isFloat(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isDouble() throws NoSuchFieldException {
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedByte")));
        assertTrue(Utils.isDouble(RealmModel.class.getDeclaredField("aDouble")));
        assertTrue(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isDouble(RealmModel.class.getDeclaredField("aBoxedFloat")));
    }

    @Test
    public void isBoolean() throws NoSuchFieldException {
        assertTrue(Utils.isBoolean(RealmModel.class.getDeclaredField("aBool")));
        assertTrue(Utils.isBoolean(RealmModel.class.getDeclaredField("aBoxedBool")));
    }

    @Test
    public void isString() throws NoSuchFieldException {
        assertTrue(Utils.isString(RealmModel.class.getDeclaredField("aString")));
    }

    @Test
    public void isBlob() throws NoSuchFieldException {
        assertTrue(Utils.isBlob(RealmModel.class.getDeclaredField("aBlob")));
    }

    @Test
    public void isDate() throws NoSuchFieldException {
        assertTrue(Utils.isDate(RealmModel.class.getDeclaredField("aDate")));
    }

    @Test
    public void isParametrizedField() throws NoSuchFieldException {
        assertTrue(Utils.isParametrizedField(RealmModel.class.getDeclaredField("aRealmList")));
        assertTrue(Utils.isParametrizedField(RealmModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isRealmObjectField() throws NoSuchFieldException {
        assertTrue(Utils.isRealmObjectField(RealmModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isRealmObjectField(RealmModel.class.getDeclaredField("aRealmList")));
    }
}
