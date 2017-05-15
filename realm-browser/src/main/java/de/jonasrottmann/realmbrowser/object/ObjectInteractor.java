package de.jonasrottmann.realmbrowser.object;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractorImpl;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import io.realm.DynamicRealm;
import io.realm.RealmModel;

import static de.jonasrottmann.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_CLASS;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ObjectInteractor extends BaseInteractorImpl<ObjectContract.Presenter> implements ObjectContract.Interactor {
    @Nullable
    private Class<? extends RealmModel> realmModelClass = null;
    @Nullable
    private DynamicRealm dynamicRealm;

    ObjectInteractor(ObjectContract.Presenter presenter) {
        super(presenter);
    }

    //region InteractorInput
    @Override
    public void requestForContentUpdate(@Nullable DynamicRealm dynamicRealm) {
        if (dynamicRealm == null || dynamicRealm.isClosed()) return;
        this.dynamicRealm = dynamicRealm;
        this.realmModelClass = (Class<? extends RealmModel>) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_CLASS);
        if (realmModelClass == null) {
            throw new IllegalStateException("Realm model class must be set in DataHolder.");
        }
        getPresenter().updateWithTitle(realmModelClass.getSimpleName()); // TODO new?!
        getPresenter().updateWithDeleteActionShown(true); // TODO new?!
    }

    @Override
    public void onDeleteObjectActionSelected() {
        if (realmModelClass != null) {
            getPresenter().showDeleteConformationDialog(realmModelClass.getSimpleName());
        }
    }

    @Override
    public void onDeleteObjectConfirmSelected() {
        // TODO delete...
    }
    //endregion

    //region Helper

    //endregion
}
