package de.jonasrottmann.realmbrowser.object;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;
import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;
import io.realm.DynamicRealm;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface ObjectContract {

    interface View extends BaseView<Presenter> {
        Context getViewContext();

        void updateWithFieldViewPojos(@NonNull ArrayList<FieldViewPojo> fieldViewPojos);

        void updateWithFieldViewPojo(@NonNull FieldViewPojo fieldViewPojo);

        void updateWithTitle(@NonNull String title);

        void updateWithDeleteActionShown(boolean shown);

        void showSavedSuccessfully();
    }

    interface Presenter extends BasePresenter<View> {
        //region InteractorOutput
        void updateWithFieldViewPojos(@NonNull ArrayList<FieldViewPojo> fieldViewPojos);

        void updateWithFieldViewPojo(@NonNull FieldViewPojo fieldViewPojo);

        void updateWithTitle(@NonNull String title);

        void updateWithDeleteActionShown(boolean shown);

        void showDeleteConformationDialog(@NonNull String className);

        void showSavedSuccessfully();
        //endregion

        //region ViewOutput
        void requestForContentUpdate(@Nullable DynamicRealm realm, boolean newObject);

        void onDeleteObjectActionSelected();

        void onSaveObjectActionSelected();

        void onFieldViewValueUpdated(@NonNull FieldViewPojo fieldViewPojo);
        //endregion
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@Nullable DynamicRealm realm, boolean newObject);

        void onDeleteObjectActionSelected();

        void onDeleteObjectConfirmSelected();

        void onSaveObjectActionSelected();

        void onFieldViewValueUpdated(@NonNull FieldViewPojo fieldViewPojo);
    }
}