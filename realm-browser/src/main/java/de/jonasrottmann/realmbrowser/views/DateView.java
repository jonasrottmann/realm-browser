package de.jonasrottmann.realmbrowser.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Date;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

public class DateView extends FieldView {

    private TextView textView;
    private EditText editText;
    private ImageView infoImageView;
    private Button buttonPicker;
    private Button buttonNow;
    private boolean isInputValid;
    private Date newDateValue;

    public DateView(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isDate(getField())) throw new IllegalArgumentException();
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_date);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = (TextView) findViewById(R.id.realm_browser_field_date_textview);
        editText = (EditText) findViewById(R.id.realm_browser_field_date_edittext);
        buttonPicker = (Button) findViewById(R.id.realm_browser_field_date_button_picker);
        buttonNow = (Button) findViewById(R.id.realm_browser_field_date_button_now);
        infoImageView = (ImageView) findViewById(R.id.realm_browser_field_date_infoimageview);

        editText.addTextChangedListener(createTextWatcher());

        buttonPicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
        buttonNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                newDateValue = new Date(System.currentTimeMillis());
                editText.setText(String.valueOf(newDateValue.getTime()));
                textView.setText(newDateValue.toString());
            }
        });
        infoImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(DateView.this, "Time in milliseconds since epoch.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void toggleEditMode(boolean enable) {
        editText.setVisibility(enable ? VISIBLE : GONE);
        infoImageView.setVisibility(enable ? VISIBLE : GONE);
        buttonPicker.setVisibility(enable ? VISIBLE : GONE);
        buttonNow.setVisibility(enable ? VISIBLE : GONE);
    }

    @Override
    public boolean isInputValid() {
        return isInputValid;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.isDate(getField())) {
            editText.setText(String.valueOf(realmObject.getDate(getField().getName()).getTime()));
            textView.setText(realmObject.getDate(getField().getName()).toString());
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
                        newDateValue = new Date(Long.parseLong(s.toString()));
                        textView.setText(newDateValue.toString());
                    }
                    getFieldInfoImageView().setVisibility(GONE);
                    isInputValid = true;
                    DateView.this.setBackgroundColor(getColor(getContext(), android.R.color.transparent));
                } catch (NumberFormatException e) {
                    isInputValid = false;
                    getFieldInfoImageView().setVisibility(VISIBLE);
                    getFieldInfoImageView().setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_warning_black_24dp));
                    getFieldInfoImageView().setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
                    getFieldInfoImageView().setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(DateView.this, s.toString() + " does not fit data type " + getField().getType().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    DateView.this.setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
                }
            }
        };
    }
}
