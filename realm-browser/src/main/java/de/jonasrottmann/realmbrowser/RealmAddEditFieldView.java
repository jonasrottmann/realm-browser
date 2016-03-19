package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

import io.realm.RealmFieldType;
import io.realm.RealmObjectSchema;

class RealmAddEditFieldView extends LinearLayout {

    private TextView fieldName, fieldType;
    private ImageView fieldInfoView;
    private CheckBox fieldIsNull;
    private Spinner fieldBoolSpinner;
    private EditText fieldEditText;

    private Field mField;

    public RealmAddEditFieldView(Context context) {
        super(context);
        initViews(context, null);
    }

    public RealmAddEditFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public RealmAddEditFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, @Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.realm_browser_addedit_field_view, this);

        // Setup Views
        fieldName = (TextView) this.findViewById(R.id.realm_browser_field_name);
        fieldType = (TextView) this.findViewById(R.id.realm_browser_field_type);
        fieldIsNull = (CheckBox) this.findViewById(R.id.realm_browser_field_setnull);
        fieldInfoView = (ImageView) this.findViewById(R.id.realm_browser_field_info);
        fieldBoolSpinner = (Spinner) this.findViewById(R.id.realm_browser_field_boolspinner);
        fieldEditText = (EditText) this.findViewById(R.id.realm_browser_field_edittext);

        // Init Views
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.realm_browser_boolean, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldBoolSpinner.setAdapter(adapter);
    }

    public void setField(@NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        mField = field;
        fieldName.setText(field.getName());
        fieldType.setText(field.getType().getName());

        if (realmObjectSchema.isNullable(field.getName()) && realmObjectSchema.getFieldType(field.getName()) != RealmFieldType.LIST) {
            fieldIsNull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    fieldEditText.setEnabled(!isChecked);
                    fieldBoolSpinner.setEnabled(!isChecked);
                }
            });
        } else {
            fieldIsNull.setVisibility(GONE);
        }

        if (realmObjectSchema.isPrimaryKey(field.getName())) {
            fieldInfoView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24px));
            fieldInfoView.setVisibility(VISIBLE);
        } else if (realmObjectSchema.isRequired(field.getName())) {
            // TODO
        } else {
            fieldInfoView.setVisibility(GONE);
        }


        // TODO show edit text / spinner / textview
        if (field.getType().getName().equals(String.class.getName())) {
            fieldEditText.setVisibility(VISIBLE);
            fieldEditText.setMaxLines(4);
        } else if (field.getType().getName().equals(Boolean.class.getName()) || field.getType().getName().equals("boolean")) {
            fieldBoolSpinner.setVisibility(VISIBLE);
        } else if (field.getType().getName().equals(Integer.class.getName()) || field.getType().getName().equals("int")) {
            fieldEditText.setVisibility(VISIBLE);
            fieldEditText.setMaxLines(1);
            fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public Field getField() {
        return mField;
    }

    @Nullable
    public Object getValue() {
        if (mField.getType().getName().equals(String.class.getName())) {
            return fieldEditText.getText().toString();
        } else if (mField.getType().getName().equals(Boolean.class.getName()) || mField.getType().getName().equals("boolean")) {
            return Boolean.parseBoolean(fieldBoolSpinner.getSelectedItem().toString());
        } else if (mField.getType().getName().equals(Integer.class.getName()) || mField.getType().getName().equals("int")) {
            return fieldEditText.getText().toString();
        } else if (mField.getType().getName().equals(Long.class.getName()) || mField.getType().getName().equals("long")) {
            return Long.parseLong(fieldEditText.getText().toString());
        }
        return null;
    }

    public boolean getIsNull() {
        return fieldIsNull.isChecked();
    }

    public void togglePrimaryKeyError(boolean show) {
        fieldInfoView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24px));
        if (show) {
            fieldInfoView.setColorFilter(ContextCompat.getColor(getContext(), R.color.realm_browser_error), PorterDuff.Mode.SRC_ATOP);
            fieldInfoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(RealmAddEditFieldView.this, "Primary key \"" + getValue() + "\" already exists.", Snackbar.LENGTH_SHORT).show();
                }
            });
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.realm_browser_error_light));
        } else {
            fieldInfoView.setColorFilter(null);
            fieldInfoView.setOnClickListener(null);
            setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }
    }
}
