package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.utils.MagicUtils;

class RealmAddEditFieldView extends LinearLayout {

    private TextView fieldName, fieldType;
    private CheckBox fieldIsNull;
    private Spinner fieldBoolSpinner;
    private EditText fieldEditText;
    @Nullable private Field field;


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
        fieldBoolSpinner = (Spinner) this.findViewById(R.id.realm_browser_field_boolspinner);
        fieldEditText = (EditText) this.findViewById(R.id.realm_browser_field_edittext);

        // Init Views
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.realm_browser_boolean, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldBoolSpinner.setAdapter(adapter);
    }

    public void setField(@NonNull Field field) {
        this.field = field;

        fieldName.setText(field.getName());
        fieldType.setText(field.getType().getName());

        if (!MagicUtils.isFieldNullable(field)) {
            fieldIsNull.setVisibility(GONE);
        } else {
            fieldIsNull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    fieldEditText.setEnabled(!isChecked);
                    fieldBoolSpinner.setEnabled(!isChecked);
                }
            });
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
        return field;
    }

    public Object getValue() {
        return null;
    }

    public boolean getIsNull() {
        return fieldIsNull.isChecked();
    }
}
