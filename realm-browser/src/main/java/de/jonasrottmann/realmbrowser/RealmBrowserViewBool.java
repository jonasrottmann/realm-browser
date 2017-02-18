package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;

class RealmBrowserViewBool extends RealmBrowserViewField {
    private Spinner spinner;

    public RealmBrowserViewBool(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isBoolean(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_spinner);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        spinner = (Spinner) findViewById(R.id.realm_browser_field_boolspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.realm_browser_boolean, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public Object getValue() {
        return Boolean.parseBoolean(spinner.getSelectedItem().toString());
    }

    @Override
    public void toggleInputMode(boolean enable) {
        spinner.setEnabled(enable);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.isBoolean(getField())) {
            spinner.setSelection(realmObject.getBoolean(getField().getName()) ? 0 : 1);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
