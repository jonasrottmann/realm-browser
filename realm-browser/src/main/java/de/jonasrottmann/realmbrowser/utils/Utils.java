package de.jonasrottmann.realmbrowser.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;

public class Utils {
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

    @Nullable
    public static String createBlobValueString(@NonNull DynamicRealmObject realmObject, @NonNull Field field) {
        if (realmObject.getBlob(field.getName()) == null)
            return null;

        StringBuilder builder = new StringBuilder("byte[] = ");
        builder.append("{");
        for (int i = 0; i < realmObject.getBlob(field.getName()).length; i++) {
            builder.append(String.valueOf(realmObject.getBlob(field.getName())[i]));
            if (i < realmObject.getBlob(field.getName()).length - 1)
                builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

    @NonNull
    public static CharSequence getFieldValueString(@NonNull DynamicRealmObject realmObject, Field field) {
        String value = null;
        if (isParametrizedField(field)) {
            // RealmList
            value = createParametrizedName(field);
        } else if (isBlob(field)) {
            // byte[]
            value = createBlobValueString(realmObject, field);
        } else {
            // Strings, Numbers, Objects
            if (realmObject.get(field.getName()) != null)
                value = String.valueOf(realmObject.get(field.getName()));
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

    @Nullable
    public static String getPrimaryKeyFieldName(@NonNull RealmObjectSchema schema) {
        for (String s : schema.getFieldNames()) {
            if (schema.isPrimaryKey(s))
                return s;
        }
        return null;
    }


    public static boolean isNumberField(@NonNull Field field) {
        return isLong(field)
                || isInteger(field)
                || isShort(field)
                || isByte(field)
                || isDouble(field)
                || isFloat(field);
    }

    public static boolean isLong(@NonNull Field field) {
        return field.getType().getName().equals(Long.class.getName()) || field.getType().getName().equals("long");
    }

    public static boolean isInteger(@NonNull Field field) {
        return field.getType().getName().equals(Integer.class.getName()) || field.getType().getName().equals("int");
    }

    public static boolean isShort(@NonNull Field field) {
        return field.getType().getName().equals(Short.class.getName()) || field.getType().getName().equals("short");
    }

    public static boolean isByte(@NonNull Field field) {
        return field.getType().getName().equals(Byte.class.getName()) || field.getType().getName().equals("byte");
    }

    public static boolean isDouble(@NonNull Field field) {
        return field.getType().getName().equals(Double.class.getName()) || field.getType().getName().equals("double");
    }

    public static boolean isFloat(@NonNull Field field) {
        return field.getType().getName().equals(Float.class.getName()) || field.getType().getName().equals("float");
    }

    public static boolean isBoolean(@NonNull Field field) {
        return field.getType().getName().equals(Boolean.class.getName()) || field.getType().getName().equals("boolean");
    }

    public static boolean isString(@NonNull Field field) {
        return field.getType().getName().equals(String.class.getName());
    }

    public static boolean isParametrizedField(@NonNull Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }

    public static boolean isBlob(@NonNull Field field) {
        return field.getType().getName().equals(byte[].class.getName());
    }

    public static boolean isDate(@NonNull Field field) {
        return field.getType().getName().equals(Date.class.getName());
    }

    public static boolean isRealmObjectField(@NonNull Field field) {
        return RealmObject.class.isAssignableFrom(field.getType());
    }
}