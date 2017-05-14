package de.jonasrottmann.realmbrowser.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.basemvp.BaseInteractorImpl;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.helper.RealmPreferences;
import de.jonasrottmann.realmbrowser.helper.Utils;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import timber.log.Timber;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_CLASS;
import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_FIELD;
import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BrowserInteractor extends BaseInteractorImpl<BrowserContract.Presenter> implements BrowserContract.Interactor {
    @Nullable
    private Class<? extends RealmModel> realmModelClass = null;
    @Nullable
    private DynamicRealm dynamicRealm;
    @Nullable
    private List<Field> fields;
    @Nullable
    private ArrayList<Integer> selectedFieldIndices;

    BrowserInteractor(BrowserContract.Presenter presenter) {
        super(presenter);
    }

    //region InteractorInput
    @Override
    public void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, int displayMode) {
        if (dynamicRealm == null || dynamicRealm.isClosed()) return;
        this.dynamicRealm = dynamicRealm;

        if (displayMode == BrowserContract.DisplayMode.REALM_CLASS) {
            this.realmModelClass = (Class<? extends RealmModel>) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_CLASS);
            getPresenter().updateWithRealmObjects(dynamicRealm.where(this.realmModelClass.getSimpleName()).findAll());
        } else if (displayMode == BrowserContract.DisplayMode.REALM_LIST) {
            DynamicRealmObject dynamicRealmObject = (DynamicRealmObject) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_OBJECT);
            Field field = (Field) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_FIELD);
            if (dynamicRealmObject != null && field != null) {
                getPresenter().updateWithRealmObjects(dynamicRealmObject.getList(field.getName()));
                if (Utils.isParametrizedField(field)) {
                    this.realmModelClass = (Class<? extends RealmObject>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                } else {
                    throw new IllegalStateException("This field must be parametrized.");
                }
            } else {
                throw new IllegalStateException("No object or field have been saved to DataHolder.");
            }
        } else {
            throw new IllegalStateException("Unsupported display mode.");
        }

        getPresenter().updateWithFABVisibility(this.realmModelClass != null);

        // Update Title
        getPresenter().updateWithTitle(String.format("%s", this.realmModelClass.getSimpleName()));

        // Update Fields
        fields = getFieldsList(dynamicRealm, this.realmModelClass);
        selectedFieldIndices = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            if (i < 3) selectedFieldIndices.add(i);
        }
        updateSelectedFields();

        // Wrap
        getPresenter().updateWithTextWrap(new RealmPreferences(context).shouldWrapText());
    }

    @Override
    public void onWrapTextOptionToggled(@NonNull Context context) {
        RealmPreferences realmPreferences = new RealmPreferences(context);
        realmPreferences.setShouldWrapText(!realmPreferences.shouldWrapText());
        getPresenter().updateWithTextWrap(realmPreferences.shouldWrapText());
    }

    @Override
    public void onNewObjectSelected() {
        if (this.realmModelClass != null) {
            getPresenter().showNewObjectActivity(this.realmModelClass);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onInformationSelected() {
        if (dynamicRealm != null && !dynamicRealm.isClosed() && realmModelClass != null) {
            getPresenter().showInformation(dynamicRealm.where(realmModelClass.getSimpleName()).count());
        }
    }

    @Override
    public void onAboutSelected(@NonNull Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.realm_browser_git))));
    }

    @Override
    public void onRowSelected(@NonNull DynamicRealmObject dynamicRealmObject) {
        if (this.realmModelClass != null) {
            DataHolder.getInstance().save(DATA_HOLDER_KEY_OBJECT, dynamicRealmObject);
            getPresenter().showObjectActivity(this.realmModelClass);
        }
    }

    @Override
    public void onFieldSelectionChanged(int fieldIndex, boolean checked) {
        if (selectedFieldIndices != null) {
            if (checked && !selectedFieldIndices.contains(fieldIndex)) {
                selectedFieldIndices.add(fieldIndex);
            } else if (selectedFieldIndices.contains(fieldIndex)) {
                selectedFieldIndices.remove((Integer) fieldIndex);
            }
            updateSelectedFields();
        }
    }
    //endregion


    //region Helper
    @NonNull
    private static List<Field> getFieldsList(@NonNull DynamicRealm dynamicRealm, @NonNull Class<? extends RealmModel> realmModelClass) {
        RealmObjectSchema schema = dynamicRealm.getSchema().get(realmModelClass.getSimpleName());
        ArrayList<Field> fieldsList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                fieldsList.add(realmModelClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d(e, "Initializing field map.");
            }
        }
        return fieldsList;
    }

    private void updateSelectedFields() {
        if (selectedFieldIndices != null && fields != null) {
            Integer[] selectedFieldIndicesArray = new Integer[selectedFieldIndices.size()];
            selectedFieldIndices.toArray(selectedFieldIndicesArray);
            getPresenter().updateWithFieldList(fields, selectedFieldIndicesArray);
        }
    }
    //endregion
}
