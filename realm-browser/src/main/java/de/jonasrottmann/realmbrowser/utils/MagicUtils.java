package de.jonasrottmann.realmbrowser.utils;

import android.support.annotation.NonNull;

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
    public static String getFieldValueString(DynamicRealmObject realmObject, Field field) {
        if (field.getType().getName().equals(Byte.class.getName()) || field.getType().getName().equals("byte")) {
            // Byte
            return (String.valueOf(realmObject.getByte(field.getName())));
        } else if (field.getType().getName().equals(Boolean.class.getName()) || field.getType().getName().equals("boolean")) {
            // Boolean
            return (String.valueOf(realmObject.getBoolean(field.getName())));
        } else if (field.getType().getName().equals(Short.class.getName()) || field.getType().getName().equals("short")) {
            // Short
            return (String.valueOf(realmObject.getShort(field.getName())));
        } else if (field.getType().getName().equals(Integer.class.getName()) || field.getType().getName().equals("int")) {
            // Integer
            return (String.valueOf(realmObject.getInt(field.getName())));
        } else if (field.getType().getName().equals(Long.class.getName()) || field.getType().getName().equals("long")) {
            // Long
            return (String.valueOf(realmObject.getLong(field.getName())));
        } else if (field.getType().getName().equals(Float.class.getName()) || field.getType().getName().equals("float")) {
            // Float
            return (String.valueOf(realmObject.getFloat(field.getName())));
        } else if (field.getType().getName().equals(Double.class.getName()) || field.getType().getName().equals("double")) {
            // Double
            return (String.valueOf(realmObject.getDouble(field.getName())));
        } else if (field.getType().getName().equals(String.class.getName())) {
            // String
            return (realmObject.getString(field.getName()));
        } else if (field.getType().getName().equals(Date.class.getName())) {
            // Date
            return (realmObject.getDate(field.getName()).toString());
        } else {
            if (field.getType().getName().equals(RealmList.class.getName())) {
                // RealmList
                return (MagicUtils.createParametrizedName(field));
            } else {
                // ? extends RealmObject
                return (realmObject.getObject(field.getName()).toString());
            }
        }
    }
}
