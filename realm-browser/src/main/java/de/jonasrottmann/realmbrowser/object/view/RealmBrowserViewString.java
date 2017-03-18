package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewStub;
import android.widget.EditText;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RealmBrowserViewString extends RealmBrowserViewField {

    EditText fieldEditText;

    public RealmBrowserViewString(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isString(getField())) {
            throw new IllegalArgumentException();
        }
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
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }
        return fieldEditText.getText().toString();
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        fieldEditText.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.isString(getField())) {
            if (realmObject.getString(getField().getName()) == null) {
                updateFieldIsNullCheckBoxValue(true);
            } else {
                fieldEditText.setText(realmObject.getString(getField().getName()));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}