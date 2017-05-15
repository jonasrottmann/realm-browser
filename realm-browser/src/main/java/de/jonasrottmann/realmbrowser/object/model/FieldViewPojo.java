package de.jonasrottmann.realmbrowser.object.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;

public class FieldViewPojo {
    @NonNull
    private final Field field;
    private final boolean isPrimaryKey;
    private final boolean isPrimaryKeyError;
    private final boolean isNullable;
    @Nullable
    private Object value;
    private final boolean hasNumberOverflowError;

    public FieldViewPojo(@NonNull Field field, boolean isPrimaryKey, boolean isPrimaryKeyError, boolean isNullable, @Nullable Object value, boolean hasNumberOverflowError) {
        this.field = field;
        this.isPrimaryKey = isPrimaryKey;
        this.isPrimaryKeyError = isPrimaryKeyError;
        this.isNullable = isNullable;
        this.value = value;
        this.hasNumberOverflowError = hasNumberOverflowError;
    }

    public boolean isPrimaryKeyError() {
        return isPrimaryKeyError;
    }

    @NonNull
    public Field getField() {
        return field;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isNullable() {
        return isNullable;
    }

    @Nullable
    public Object getValue() {
        return value;
    }

    public boolean isHasNumberOverflowError() {
        return hasNumberOverflowError;
    }

    public void setValue(@Nullable Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FieldViewPojo)) {
            return false;
        }
        FieldViewPojo view = (FieldViewPojo) obj;
        return view.field.equals(field);
    }
}
