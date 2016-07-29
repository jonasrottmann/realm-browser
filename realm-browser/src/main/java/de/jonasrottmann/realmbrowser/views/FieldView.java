package de.jonasrottmann.realmbrowser.views;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.jonasrottmann.realmbrowser.R;
import io.realm.DynamicRealmObject;
import io.realm.RealmFieldType;
import io.realm.RealmObjectSchema;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

/**
 * Created by jonas on 26/07/16.
 */
public abstract class FieldView extends LinearLayout {
    private TextView tvFieldName;
    private TextView tvFieldType;
    private ImageView ivFieldPrimaryKey;
    private ImageView ivFieldInfo;
    private CheckBox cbxFieldIsNull;
    private Field mField;
    private RealmObjectSchema mRealmObjectSchema;


    public FieldView(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context);

        mRealmObjectSchema = realmObjectSchema;
        mField = field;

        initHeaderViews(context);
        inflateViewStub();
        initViewStubView();

        tvFieldName.setText(field.getName());
        tvFieldType.setText(getFieldTypeString());

        setupNullableCheckBox(field);
        setupPrimaryKeyImageView(field);
    }

    private void initHeaderViews(Context context) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.realm_browser_fieldview, this);

        // Setup Header Views
        tvFieldName = (TextView) this.findViewById(R.id.realm_browser_field_name);
        tvFieldType = (TextView) this.findViewById(R.id.realm_browser_field_type);
        cbxFieldIsNull = (CheckBox) this.findViewById(R.id.realm_browser_field_setnull);
        ivFieldPrimaryKey = (ImageView) this.findViewById(R.id.realm_browser_field_primarykey);
        ivFieldInfo = (ImageView) this.findViewById(R.id.realm_browser_field_info);
    }

    private void setupPrimaryKeyImageView(@NonNull Field field) {
        if (mRealmObjectSchema.isPrimaryKey(field.getName())) {
            ivFieldPrimaryKey.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24dp));
            ivFieldPrimaryKey.setVisibility(VISIBLE);
        } else if (mRealmObjectSchema.isRequired(field.getName())) {
            // TODO set key drawable with star
        } else {
            ivFieldPrimaryKey.setVisibility(GONE);
        }
    }

    private void setupNullableCheckBox(@NonNull Field field) {
        if (mRealmObjectSchema.isNullable(field.getName()) && mRealmObjectSchema.getFieldType(field.getName()) != RealmFieldType.LIST) {
            cbxFieldIsNull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleEditMode(!isChecked);
                }
            });
        } else {
            cbxFieldIsNull.setVisibility(GONE);
        }
    }

    public void togglePrimaryKeyError(boolean show) {
        ivFieldPrimaryKey.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24dp));
        if (show) {
            ivFieldPrimaryKey.setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
            ivFieldPrimaryKey.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(FieldView.this, "Primary key \"" + getValue() + "\" already exists.", Snackbar.LENGTH_SHORT).show();
                }
            });
            setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
        } else {
            ivFieldPrimaryKey.setColorFilter(null);
            ivFieldPrimaryKey.setOnClickListener(null);
            setBackgroundColor(getColor(getContext(), android.R.color.transparent));
        }
    }


    /* *********************
     * Methods to override *
     * *********************/

    /**
     * TODO override for realmlist fields, binary etc...
     *
     * @return
     */
    protected String getFieldTypeString() {
        return getField().getType().getSimpleName();
    }

    /**
     *
     */
    public abstract void inflateViewStub();

    /**
     * Called after ViewStub has been inflated.
     * Do findViewById() here to retrieve the view.
     */
    public abstract void initViewStubView();

    /**
     * @return
     */
    public abstract Object getValue();

    /**
     * @param enable
     */
    @CallSuper
    public void toggleEditMode(boolean enable) {
        getFieldIsNullCheckBox().setEnabled(enable);
    };

    /**
     * @return
     */
    public abstract boolean isInputValid();

    /**
     * @param realmObject
     */
    public abstract void setRealmObject(@NonNull DynamicRealmObject realmObject);


    /* ********
     * Getter *
     * ********/

    protected RealmObjectSchema getRealmObjectSchema() {
        return mRealmObjectSchema;
    }

    public Field getField() {
        return mField;
    }

    protected CheckBox getFieldIsNullCheckBox() {
        return cbxFieldIsNull;
    }

    protected ImageView getFieldInfoImageView() {
        return ivFieldInfo;
    }
}