package de.jonasrottmann.realmbrowser.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import io.realm.DynamicRealmObject;
import io.realm.RealmList;

public class MagicUtils {
    public static boolean isParametrizedField(@NonNull Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }

    @NonNull
    public static String createParametrizedName(@NonNull Field field) {
        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        String rawType = pType.getRawType().toString();
        int rawTypeIndex = rawType.lastIndexOf(".");
        if (rawTypeIndex > 0) {
            rawType = rawType.substring(rawTypeIndex + 1);
        }

        String argument = pType.getActualTypeArguments()[0].toString();
        int argumentIndex = argument.lastIndexOf(".");
        if (argumentIndex > 0) {
            argument = argument.substring(argumentIndex + 1);
        }

        return rawType + "<" + argument + ">";
    }

    @NonNull
    public static CharSequence getFieldValueString(DynamicRealmObject realmObject, Field field) {
        String value;
        if (field.getType().getName().equals(Byte.class.getName()) || field.getType().getName().equals("byte")) {
            // Byte
            value = String.valueOf(realmObject.getByte(field.getName()));
        } else if (field.getType().getName().equals(Boolean.class.getName()) || field.getType().getName().equals("boolean")) {
            // Boolean
            value = String.valueOf(realmObject.getBoolean(field.getName()));
        } else if (field.getType().getName().equals(Short.class.getName()) || field.getType().getName().equals("short")) {
            // Short
            value = String.valueOf(realmObject.getShort(field.getName()));
        } else if (field.getType().getName().equals(Integer.class.getName()) || field.getType().getName().equals("int")) {
            // Integer
            value = String.valueOf(realmObject.getInt(field.getName()));
        } else if (field.getType().getName().equals(Long.class.getName()) || field.getType().getName().equals("long")) {
            // Long
            value = String.valueOf(realmObject.getLong(field.getName()));
        } else if (field.getType().getName().equals(Float.class.getName()) || field.getType().getName().equals("float")) {
            // Float
            value = String.valueOf(realmObject.getFloat(field.getName()));
        } else if (field.getType().getName().equals(Double.class.getName()) || field.getType().getName().equals("double")) {
            // Double
            value = String.valueOf(realmObject.getDouble(field.getName()));
        } else if (field.getType().getName().equals(String.class.getName())) {
            // String
            value = realmObject.getString(field.getName());
        } else if (field.getType().getName().equals(Date.class.getName())) {
            // Date
            value = (realmObject.getDate(field.getName()).toString());
        } else {
            if (field.getType().getName().equals(RealmList.class.getName())) {
                // RealmList
                value = (MagicUtils.createParametrizedName(field));
            } else {
                // ? extends RealmObject
                value = (realmObject.getObject(field.getName()).toString());
            }
        }

        if (value == null) {
            // Display null in italics to be able to distinguish between null and a string that actually says "null"
            value = "null";
            SpannableString nullString = new SpannableString(value);
            nullString.setSpan(new StyleSpan(Typeface.ITALIC), 0, value.length(), 0);
            return nullString;
        } else {
            return value;
        }
    }
}
