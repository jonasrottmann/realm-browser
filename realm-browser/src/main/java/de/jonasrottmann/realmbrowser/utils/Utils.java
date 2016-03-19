package de.jonasrottmann.realmbrowser.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import io.realm.DynamicRealmObject;
import io.realm.RealmList;
import io.realm.RealmObjectSchema;

public class Utils {
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
    public static CharSequence getFieldValueString(@NonNull DynamicRealmObject realmObject, Field field) {
        String value;
        if (field.getType().getName().equals(RealmList.class.getName())) {
            // RealmList
            value = (Utils.createParametrizedName(field));
        } else {
            // ? extends RealmObject
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
}
