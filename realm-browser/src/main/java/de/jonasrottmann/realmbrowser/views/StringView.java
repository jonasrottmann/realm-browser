package de.jonasrottmann.realmbrowser.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewStub;
import android.widget.EditText;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

public class StringView extends FieldView {

    EditText fieldEditText;

    public StringView(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isString(getField())) throw new IllegalArgumentException();
    }

    @Override
    public String getFieldTypeString() {
        return getField().getType().getSimpleName();
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_edittext);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        fieldEditText = (EditText) findViewById(R.id.realm_browser_field_edittext);
        fieldEditText.setMaxLines(4);
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && getFieldIsNullCheckBox().isChecked())
            return null;

        return fieldEditText.getText().toString();
    }

    @Override
    public void toggleEditMode(boolean enable) {
        fieldEditText.setEnabled(enable);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.isString(getField())) {
            fieldEditText.setText(realmObject.getString(getField().getName()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}