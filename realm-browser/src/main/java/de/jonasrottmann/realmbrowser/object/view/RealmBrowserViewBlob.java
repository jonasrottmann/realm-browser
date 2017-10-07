package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewStub;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

import static de.jonasrottmann.realmbrowser.extensions.ByteArrayUtils.createBlobValueString;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isBlob;


@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RealmBrowserViewBlob extends RealmBrowserViewField {

    private TextView textView;
    private DynamicRealmObject realmObject;

    public RealmBrowserViewBlob(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!isBlob(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_textview);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = findViewById(R.id.realm_browser_field_textview);
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }
        return realmObject.getBlob(getField().getName());
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        textView.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (isBlob(getField())) {
            this.realmObject = realmObject;
            if (realmObject.getBlob(getField().getName()) == null) {
                updateFieldIsNullCheckBoxValue(true);
            } else {
                textView.setText(createBlobValueString(realmObject.getBlob(getField().getName()), 1000));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
