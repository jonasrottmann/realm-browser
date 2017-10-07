package de.jonasrottmann.realmbrowser.object.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isByte;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isDouble;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isFloat;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isInteger;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isLong;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isNumber;
import static de.jonasrottmann.realmbrowser.extensions.FieldUtils.isShort;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RealmBrowserViewNumber extends RealmBrowserViewField {
    private boolean isInputValid = true;
    private EditText fieldEditText;

    public RealmBrowserViewNumber(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!isNumber(getField())) {
            throw new IllegalArgumentException();
        }
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
        fieldEditText.setMaxLines(1);
        fieldEditText.addTextChangedListener(createTextWatcher());
        if (isDouble(getField()) || isFloat(getField())) {
            fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        } else {
            fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }

        String value = fieldEditText.getText().toString();
        if (value.isEmpty()) {
            value = "0";
        }

        if (isByte(getField())) {
            return Byte.parseByte(value);
        } else if (isShort(getField())) {
            return Short.parseShort(value);
        } else if (isInteger(getField())) {
            return Integer.parseInt(value);
        } else if (isLong(getField())) {
            return Long.parseLong(value);
        } else if (isDouble(getField())) {
            return Double.parseDouble(value);
        } else if (isFloat(getField())) {
            return Float.parseFloat(value);
        }
        return null;
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        fieldEditText.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return isInputValid;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (isNumber(getField())) {
            fieldEditText.setText(String.valueOf(realmObject.get(getField().getName())));
        } else {
            throw new IllegalArgumentException();
        }
    }


    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void afterTextChanged(final Editable s) {
                try {
                    if (!s.toString().isEmpty()) {
                        if (isByte(getField())) {
                            Byte.parseByte(s.toString());
                        } else if (isShort(getField())) {
                            Short.parseShort(s.toString());
                        } else if (isInteger(getField())) {
                            Integer.parseInt(s.toString());
                        } else if (isLong(getField())) {
                            Long.parseLong(s.toString());
                        } else if (isDouble(getField())) {
                            Double.parseDouble(s.toString());
                        } else if (isFloat(getField())) {
                            Float.parseFloat(s.toString());
                        }
                    }
                    getFieldInfoImageView().setVisibility(GONE);
                    isInputValid = true;
                    RealmBrowserViewNumber.this.setBackgroundColor(getColor(getContext(), android.R.color.transparent));
                } catch (NumberFormatException e) {
                    isInputValid = false;
                    getFieldInfoImageView().setVisibility(VISIBLE);
                    getFieldInfoImageView().setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_warning_black_24dp));
                    getFieldInfoImageView().setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
                    getFieldInfoImageView().setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(RealmBrowserViewNumber.this, s.toString() + " does not fit data type " + getField().getType().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    RealmBrowserViewNumber.this.setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
                }
            }
        };
    }
}
