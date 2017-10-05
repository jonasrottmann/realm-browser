package de.jonasrottmann.realmbrowser.helper;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import io.realm.DynamicRealmObject;

import static de.jonasrottmann.realmbrowser.extensions.ByteArray_extKt.createBlobValueString;
import static de.jonasrottmann.realmbrowser.extensions.File_extKt.isBlob;
import static de.jonasrottmann.realmbrowser.extensions.File_extKt.isParametrizedField;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class Utils {

    private Utils() {
    }

    @NonNull
    public static String createParametrizedName(@NonNull Field field) {
        //noinspection ConstantConditions
        if (field == null) {
            throw new IllegalArgumentException("The passed in Field cannot be null.");
        }

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
    public static CharSequence getFieldValueString(@NonNull DynamicRealmObject realmObject, @NonNull Field field) {
        String valueString = null;

        if (isParametrizedField(field)) {
            valueString = createParametrizedName(field);
        } else if (isBlob(field)) {
            valueString = createBlobValueString(realmObject.getBlob(field.getName()), 8);
        } else {
            // Strings, Numbers, Objects
            Object fieldValue = realmObject.get(field.getName());
            if (fieldValue != null) {
                valueString = String.valueOf(fieldValue);
            }
        }

        if (valueString == null) {
            // Display null in italics to be able to distinguish between null and a string that actually says "null"
            valueString = "null";
            SpannableString nullString = new SpannableString(valueString);
            nullString.setSpan(new StyleSpan(Typeface.ITALIC), 0, valueString.length(), 0);
            return nullString;
        } else {
            return valueString;
        }
    }

    public static boolean equals(Object a, Object b) {
        // TODO remove...
        return (a == b) || (a != null && a.equals(b));
    }
}