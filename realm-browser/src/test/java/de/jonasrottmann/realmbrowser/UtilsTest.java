package de.jonasrottmann.realmbrowser;

import de.jonasrottmann.realmbrowser.helper.Utils;
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
        Field f = RealmTestModel.class.getDeclaredField("aStringList");
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
    public void isNumber() throws NoSuchFieldException {
        assertTrue(Utils.isNumber(de.jonasrottmann.realmbrowser.RealmTestModel.class.getDeclaredField("anInteger")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aLong")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aShort")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aByte")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aDouble")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aFloat")));
        assertTrue(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isNumber(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isInteger() throws NoSuchFieldException {
        assertTrue(Utils.isInteger(de.jonasrottmann.realmbrowser.RealmTestModel.class.getDeclaredField("anInteger")));
        assertTrue(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isInteger(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isLong() throws NoSuchFieldException {
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertTrue(Utils.isLong(RealmTestModel.class.getDeclaredField("aLong")));
        assertTrue(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isLong(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isShort() throws NoSuchFieldException {
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertTrue(Utils.isShort(RealmTestModel.class.getDeclaredField("aShort")));
        assertTrue(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isShort(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isByte() throws NoSuchFieldException {
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertTrue(Utils.isByte(RealmTestModel.class.getDeclaredField("aByte")));
        assertTrue(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isByte(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isFloat() throws NoSuchFieldException {
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertTrue(Utils.isFloat(RealmTestModel.class.getDeclaredField("aFloat")));
        assertTrue(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isFloat(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isDouble() throws NoSuchFieldException {
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertTrue(Utils.isDouble(RealmTestModel.class.getDeclaredField("aDouble")));
        assertTrue(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isDouble(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isBoolean() throws NoSuchFieldException {
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertTrue(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBool")));
        assertTrue(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isBoolean(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isString() throws NoSuchFieldException {
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertTrue(Utils.isString(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isString(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isBlob() throws NoSuchFieldException {
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertTrue(Utils.isBlob(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isBlob(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isDate() throws NoSuchFieldException {
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aBlob")));
        assertTrue(Utils.isDate(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isDate(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isParametrizedField() throws NoSuchFieldException {
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("anObject")));
        assertTrue(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aString")));
        assertTrue(Utils.isParametrizedField(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isRealmObjectField() throws NoSuchFieldException {
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aDate")));
        assertTrue(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(Utils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aStringList")));
    }
}
