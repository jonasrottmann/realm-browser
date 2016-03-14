package de.jonasrottmann.realmbrowser.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class MagicUtils {
    public static boolean isParameterizedField(@NonNull Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }


    @Nullable
    public static String createParameterizedName(@NonNull Field field) {
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
}
