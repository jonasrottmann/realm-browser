package de.jonasrottmann.realmbrowser;

import org.junit.Test;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.extensions.ByteArrayUtils;
import de.jonasrottmann.realmbrowser.extensions.FieldUtils;
import de.jonasrottmann.realmbrowser.helper.Utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
    public void createBlobValueStringForEmptyBlob() {
        assertEquals("byte[] = {}", ByteArrayUtils.createBlobValueString(new byte[]{}, 0));
    }

    @Test
    public void createBlobValueStringForSingleEntryBlob() {
        assertEquals("byte[] = {1}", ByteArrayUtils.createBlobValueString(new byte[]{1}, 0));
    }

    @Test
    public void createBlobValueStringForBlob() {
        assertEquals("byte[] = {1, 2, 3, 4, 5, 6, 7, 8, 9}", ByteArrayUtils.createBlobValueString(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 0));
    }

    @Test
    public void createBlobValueStringForBlobLimited() {
        assertEquals("byte[] = {1, 2, 3, ...}", ByteArrayUtils.createBlobValueString(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 3));
    }
    //endregion

    @Test
    public void isNumber() throws NoSuchFieldException {
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("anInteger")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aLong")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aShort")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aByte")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aDouble")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aFloat")));
        assertTrue(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isNumber(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isInteger() throws NoSuchFieldException {
        assertTrue(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("anInteger")));
        assertTrue(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isInteger(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isLong() throws NoSuchFieldException {
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertTrue(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aLong")));
        assertTrue(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isLong(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isShort() throws NoSuchFieldException {
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertTrue(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aShort")));
        assertTrue(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isShort(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isByte() throws NoSuchFieldException {
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertTrue(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aByte")));
        assertTrue(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isByte(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isFloat() throws NoSuchFieldException {
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertTrue(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aFloat")));
        assertTrue(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isFloat(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isDouble() throws NoSuchFieldException {
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertTrue(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aDouble")));
        assertTrue(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isDouble(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isBoolean() throws NoSuchFieldException {
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertTrue(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBool")));
        assertTrue(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isBoolean(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isString() throws NoSuchFieldException {
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertTrue(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isString(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isBlob() throws NoSuchFieldException {
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertTrue(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isBlob(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isDate() throws NoSuchFieldException {
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aBlob")));
        assertTrue(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isDate(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isParametrizedField() throws NoSuchFieldException {
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aDate")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("anObject")));
        assertTrue(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aString")));
        assertTrue(FieldUtils.isParametrizedField(RealmTestModel.class.getDeclaredField("aStringList")));
    }

    @Test
    public void isRealmObjectField() throws NoSuchFieldException {
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("anInteger")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedInteger")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aLong")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedLong")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aShort")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedShort")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aByte")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedByte")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aDouble")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedDouble")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aFloat")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedFloat")));

        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBool")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBoxedBool")));

        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aBlob")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aDate")));
        assertTrue(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("anObject")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aRealmList")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aString")));
        assertFalse(FieldUtils.isRealmObjectField(RealmTestModel.class.getDeclaredField("aStringList")));
    }
}
