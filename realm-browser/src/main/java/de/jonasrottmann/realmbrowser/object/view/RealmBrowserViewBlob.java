package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewStub;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RealmBrowserViewBlob extends RealmBrowserViewField {

    private TextView textView;
    private DynamicRealmObject realmObject;

    public RealmBrowserViewBlob(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isBlob(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_textview);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = (TextView) findViewById(R.id.realm_browser_field_textview);
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
        if (Utils.isBlob(getField())) {
            this.realmObject = realmObject;
            if (realmObject.getBlob(getField().getName()) == null) {
                updateFieldIsNullCheckBoxValue(true);
            } else {
                textView.setText(Utils.createBlobValueString(realmObject.getBlob(getField().getName())));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
