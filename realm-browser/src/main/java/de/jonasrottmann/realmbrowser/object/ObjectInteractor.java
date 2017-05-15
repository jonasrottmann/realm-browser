package de.jonasrottmann.realmbrowser.object;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractorImpl;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmObjectSchema;
import timber.log.Timber;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_CLASS;
import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ObjectInteractor extends BaseInteractorImpl<ObjectContract.Presenter> implements ObjectContract.Interactor {
    @Nullable
    private Class<? extends RealmModel> realmModelClass = null;
    @Nullable
    private DynamicRealm dynamicRealm;
    @Nullable
    private DynamicRealmObject object = null;

    ObjectInteractor(ObjectContract.Presenter presenter) {
        super(presenter);
    }

    //region InteractorInput
    @Override
    public void requestForContentUpdate(@Nullable DynamicRealm dynamicRealm, boolean newObject) {
        if (dynamicRealm == null || dynamicRealm.isClosed()) return;
        this.dynamicRealm = dynamicRealm;
        this.realmModelClass = (Class<? extends RealmModel>) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_CLASS);
        if (realmModelClass == null) {
            throw new IllegalStateException("Realm model class must be set in DataHolder.");
        }
        if (!newObject) {
            object = (DynamicRealmObject) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_OBJECT);
            if (object == null) {
                throw new IllegalStateException("Realm object must be set in DataHolder.");
            }
        }
        getPresenter().updateWithTitle(String.format("%s%s", newObject ? "New " : "", realmModelClass.getSimpleName()));
        getPresenter().updateWithDeleteActionShown(!newObject);
        getPresenter().updateWithFieldViewPojos(createPojos());
    }

    @Override
    public void onDeleteObjectActionSelected() {
        if (realmModelClass != null) {
            getPresenter().showDeleteConformationDialog(realmModelClass.getSimpleName());
        }
    }

    @Override
    public void onDeleteObjectConfirmSelected() {
        if (dynamicRealm != null && !dynamicRealm.isClosed() && object != null && object.isValid() && object.isManaged()) {
            dynamicRealm.beginTransaction();
            object.deleteFromRealm();
            dynamicRealm.commitTransaction();
        }
        // TODO report back
    }

    @Override
    public void onSaveObjectActionSelected() {
        // TODO
        // ...
        // TODO show errors
        // ...
        getPresenter().showSavedSuccessfully();
    }

    @Override
    public void onFieldViewValueUpdated(@NonNull FieldViewPojo pojo) {
        // TODO
        // getPresenter().updateWithFieldViewPojo();
    }
    //endregion

    //region Helper
    private ArrayList<FieldViewPojo> createPojos() {
        RealmObjectSchema schema = dynamicRealm.getSchema().get(this.realmModelClass.getSimpleName());
        ArrayList<Field> classFields = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                classFields.add(realmModelClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
                Timber.d(e, "Initializing field map.");
            }
        }

        ArrayList<FieldViewPojo> pojos = new ArrayList<>();
        for (Field f : classFields) {
            FieldViewPojo pojo = new FieldViewPojo(f, schema.isPrimaryKey(f.getName()), false, schema.isNullable(f.getName()), object.get(f.getName()), false);
            pojos.add(pojo);
        }

        return pojos;
    }

    @Nullable
    private DynamicRealmObject createObject() {
//        if (dynamicRealm == null) return null;
//
//        DynamicRealmObject newRealmObject = null;
//
//        // Start Realm Transaction
//        dynamicRealm.beginTransaction();
//
//        // Create object
//        if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
//            try {
//                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
//                newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName(), fieldViewsList.get(primaryKeyFieldName).getValue());
//            } catch (IllegalArgumentException e) {
//                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
//                dynamicRealm.cancelTransaction();
//                Snackbar.make(linearLayout, "Error creating Object: IllegalArgumentException", Snackbar.LENGTH_SHORT).show();
//            } catch (RealmPrimaryKeyConstraintException e) {
//                Timber.e(e, "Error trying to create new Realm object of type %s", mRealmObjectClass.getSimpleName());
//                fieldViewsList.get(Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
//                dynamicRealm.cancelTransaction();
//                Snackbar.make(linearLayout, "Error creating Object: PrimaryKeyConstraintException", Snackbar.LENGTH_SHORT).show();
//            }
//        } else {
//            newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName());
//        }
//
//        // Commit Realm Transaction
//        if (dynamicRealm.isInTransaction()) {
//            dynamicRealm.commitTransaction();
//        }
//
//        return newRealmObject;
        return null;
    }

    private boolean saveObject() {
//        if (dynamicRealm == null || dynamicRealm.isClosed()) {
//            return false;
//        }
//
//        // Return if any field holds a invalid value
//        for (String fieldName : fieldViewsList.keySet()) {
//            if (!fieldViewsList.get(fieldName).isInputValid()) {
//                return false;
//            }
//        }
//
//        RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());
//
//        DynamicRealmObject newRealmObject = null;
//        if (currentDynamicRealmObject == null || (realmObjectSchema.hasPrimaryKey() && (!Utils.equals(currentDynamicRealmObject.get(realmObjectSchema.getPrimaryKey()), primaryKeyFieldView.getValue())))) {
//            // PK has been changed or don't have and old object to change -> create new object
//            newRealmObject = createObject();
//            if (newRealmObject == null) {
//                return false;
//            }
//        }
//
//        dynamicRealm.beginTransaction();
//        // Set values
//        for (String fieldName : fieldViewsList.keySet()) {
//            if (realmObjectSchema.isPrimaryKey(fieldName)) {
//                continue; // Prevent changing of PK, should be handled with creation of new object with new PK
//            }
//            if (!realmObjectSchema.isNullable(fieldName) && fieldViewsList.get(fieldName).getValue() == null) {
//                throw new IllegalStateException("A view which holds a nonnullable field must not return null.");
//            }
//            if (newRealmObject == null) {
//                currentDynamicRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
//            } else {
//                newRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
//            }
//        }
//
//        // Delete old object if new object was created
//        if (newRealmObject != null && currentDynamicRealmObject != null && currentDynamicRealmObject.isManaged()) {
//            currentDynamicRealmObject.deleteFromRealm();
//            currentDynamicRealmObject = newRealmObject;
//        }
//
//        // Update views
//        for (RealmBrowserViewField viewField : this.fieldViewsList.values()) {
//            viewField.setRealmObject(currentDynamicRealmObject);
//        }
//
//        dynamicRealm.commitTransaction();
//
//        // Reset primary key error
//        RealmBrowserViewField primaryKeyView = fieldViewsList.get(Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName())));
//        if (primaryKeyView != null) {
//            primaryKeyView.togglePrimaryKeyError(false);
//        }
//
//        return true;
        return true;
    }
    //endregion
}
