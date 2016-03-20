package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.RealmFieldType;
import io.realm.RealmObjectSchema;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

class RealmAddEditFieldView extends LinearLayout {

    private Field mField;
    private TextView fieldName, fieldType;
    private ImageView fieldPrimaryKeyImageView, fieldInfoImageView;
    private CheckBox fieldIsNull;
    private Spinner fieldBoolSpinner;
    private EditText fieldEditText;
    private RealmObjectSchema mRealmObjectSchema;
    private boolean isValid = true;

    public RealmAddEditFieldView(Context context) {
        super(context);
        initViews(context);
    }

    public RealmAddEditFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public RealmAddEditFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.realm_browser_addedit_field_view, this);

        // Setup Views
        fieldName = (TextView) this.findViewById(R.id.realm_browser_field_name);
        fieldType = (TextView) this.findViewById(R.id.realm_browser_field_type);
        fieldIsNull = (CheckBox) this.findViewById(R.id.realm_browser_field_setnull);
        fieldPrimaryKeyImageView = (ImageView) this.findViewById(R.id.realm_browser_field_primarykey);
        fieldInfoImageView = (ImageView) this.findViewById(R.id.realm_browser_field_info);
        fieldBoolSpinner = (Spinner) this.findViewById(R.id.realm_browser_field_boolspinner);
        fieldEditText = (EditText) this.findViewById(R.id.realm_browser_field_edittext);

        // Init Views
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.realm_browser_boolean, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldBoolSpinner.setAdapter(adapter);
    }

    public void setField(@NonNull RealmObjectSchema realmObjectSchema, @NonNull final Field field) {
        mField = field;
        mRealmObjectSchema = realmObjectSchema;

        // Set name and type
        fieldName.setText(field.getName());
        if (Utils.isParametrizedField(mField)) {
            fieldType.setText(Utils.createParametrizedName(mField));
        } else if (Utils.isBlob(mField)) {
            fieldType.setText("byte[]");
        } else {
            fieldType.setText(mField.getType().getSimpleName());
        }

        // Show/hide nullable option
        if (mRealmObjectSchema.isNullable(field.getName()) && mRealmObjectSchema.getFieldType(field.getName()) != RealmFieldType.LIST) {
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

        // Show/hide primary key indicator
        if (mRealmObjectSchema.isPrimaryKey(field.getName())) {
            fieldPrimaryKeyImageView.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24px));
            fieldPrimaryKeyImageView.setVisibility(VISIBLE);
        } else if (mRealmObjectSchema.isRequired(field.getName())) {
            // TODO
        } else {
            fieldPrimaryKeyImageView.setVisibility(GONE);
        }

        // Show/hide and set up input fields
        if (Utils.isString(mField)) {
            fieldEditText.setVisibility(VISIBLE);
            fieldEditText.setMaxLines(4);
        } else if (Utils.isBoolean(mField)) {
            fieldBoolSpinner.setVisibility(VISIBLE);
        } else if (Utils.isNumberField(mField)) {
            if (Utils.isDouble(mField) || Utils.isFloat(mField))
                fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            else
                fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

            fieldEditText.setVisibility(VISIBLE);
            fieldEditText.setMaxLines(1);
            fieldEditText.addTextChangedListener(createTextWatcherForNumberFields());
        }
    }

    public Field getField() {
        return mField;
    }

    @Nullable
    public Object getValue() {
        if (mRealmObjectSchema.isNullable(mField.getName()) && fieldIsNull.isChecked())
            return null;

        if (Utils.isString(mField)) {
            return fieldEditText.getText().toString();
        } else if (Utils.isBoolean(mField)) {
            return Boolean.parseBoolean(fieldBoolSpinner.getSelectedItem().toString());
        } else if (Utils.isNumberField(mField)) {
            String value = fieldEditText.getText().toString();
            if (value.isEmpty()) {
                value = "0";
            }
            if (Utils.isByte(mField)) {
                return Byte.parseByte(value);
            } else if (Utils.isShort(mField)) {
                return Short.parseShort(value);
            } else if (Utils.isInteger(mField)) {
                return Integer.parseInt(value);
            } else if (Utils.isLong(mField)) {
                return Long.parseLong(value);
            } else if (Utils.isDouble(mField)) {
                return Double.parseDouble(value);
            } else if (Utils.isFloat(mField)) {
                return Float.parseFloat(value);
            }
        }
        return null;
    }

    public void togglePrimaryKeyError(boolean show) {
        fieldPrimaryKeyImageView.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24px));
        if (show) {
            fieldPrimaryKeyImageView.setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
            fieldPrimaryKeyImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(RealmAddEditFieldView.this, "Primary key \"" + getValue() + "\" already exists.", Snackbar.LENGTH_SHORT).show();
                }
            });
            setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
        } else {
            fieldPrimaryKeyImageView.setColorFilter(null);
            fieldPrimaryKeyImageView.setOnClickListener(null);
            setBackgroundColor(getColor(getContext(), android.R.color.transparent));
        }
    }


    private TextWatcher createTextWatcherForNumberFields() {
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
                        if (Utils.isByte(mField)) {
                            Byte.parseByte(s.toString());
                        } else if (Utils.isShort(mField)) {
                            Short.parseShort(s.toString());
                        } else if (Utils.isInteger(mField)) {
                            Integer.parseInt(s.toString());
                        } else if (Utils.isLong(mField)) {
                            Long.parseLong(s.toString());
                        } else if (Utils.isDouble(mField)) {
                            Double.parseDouble(s.toString());
                        } else if (Utils.isFloat(mField)) {
                            Float.parseFloat(s.toString());
                        }
                    }
                    fieldInfoImageView.setVisibility(GONE);
                    isValid = true;
                    RealmAddEditFieldView.this.setBackgroundColor(getColor(getContext(), android.R.color.transparent));
                } catch (NumberFormatException e) {
                    isValid = false;
                    fieldInfoImageView.setVisibility(VISIBLE);
                    fieldInfoImageView.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_warning_black_24dp));
                    fieldInfoImageView.setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
                    fieldInfoImageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(RealmAddEditFieldView.this, s.toString() + " does not fit " + mField.getType().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    RealmAddEditFieldView.this.setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
                }
            }
        };
    }

    public boolean isValid() {
        return isValid;
    }
}
