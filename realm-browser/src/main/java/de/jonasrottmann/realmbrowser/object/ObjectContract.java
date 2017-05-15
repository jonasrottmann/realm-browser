package de.jonasrottmann.realmbrowser.object;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;
import io.realm.DynamicRealm;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface ObjectContract {

    interface View extends BaseView<Presenter> {
        Context getViewContext();

        void updateWithTitle(@NonNull String title);

        void updateWithDeleteActionShown(boolean shown);
    }

    interface Presenter extends BasePresenter<View> {
        //region InteractorOutput
        void requestForContentUpdate(@Nullable DynamicRealm realm);

        void updateWithTitle(@NonNull String title);

        void updateWithDeleteActionShown(boolean shown);

        void showDeleteConformationDialog(@NonNull String className);
        //endregion

        //region ViewOutput
        void onDeleteObjectActionSelected();
        //endregion
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@Nullable DynamicRealm realm);

        void onDeleteObjectActionSelected();

        void onDeleteObjectConfirmSelected();
    }
}