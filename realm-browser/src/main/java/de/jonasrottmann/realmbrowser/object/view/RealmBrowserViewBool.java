package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

import static de.jonasrottmann.realmbrowser.extensions.File_extKt.isBoolean;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RealmBrowserViewBool extends RealmBrowserViewField {
    private Spinner spinner;

    public RealmBrowserViewBool(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!isBoolean(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_spinner);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        spinner = findViewById(R.id.realm_browser_field_boolspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.realm_browser_boolean, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }
        return Boolean.parseBoolean(spinner.getSelectedItem().toString());
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        spinner.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (isBoolean(getField())) {
            spinner.setSelection(realmObject.getBoolean(getField().getName()) ? 0 : 1);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
