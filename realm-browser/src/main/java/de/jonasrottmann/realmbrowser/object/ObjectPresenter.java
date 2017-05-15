package de.jonasrottmann.realmbrowser.object;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BasePresenterImpl;
import de.jonasrottmann.realmbrowser.object.model.FieldViewPojo;
import io.realm.DynamicRealm;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ObjectPresenter extends BasePresenterImpl<ObjectContract.View> implements ObjectContract.Presenter {
    private final ObjectInteractor interactor;

    public ObjectPresenter() {
        interactor = new ObjectInteractor(this);
    }

    //region ViewOutput
    @Override
    public void requestForContentUpdate(@Nullable DynamicRealm realm, boolean newObject) {
        interactor.requestForContentUpdate(realm, newObject);
    }

    @Override
    public void onDeleteObjectActionSelected() {
        interactor.onDeleteObjectActionSelected();
    }

    @Override
    public void onSaveObjectActionSelected() {
        interactor.onSaveObjectActionSelected();
    }

    @Override
    public void onFieldViewValueUpdated(@NonNull FieldViewPojo pojo) {
        interactor.onFieldViewValueUpdated(pojo);
    }
    //endregion

    //region InteractorOutput
    @Override
    public void updateWithFieldViewPojos(@NonNull ArrayList<FieldViewPojo> fieldViewPojos) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithFieldViewPojos(fieldViewPojos);
        }
    }

    @Override
    public void updateWithFieldViewPojo(@NonNull FieldViewPojo fieldViewPojo) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithFieldViewPojo(fieldViewPojo);
        }
    }

    @Override
    public void updateWithTitle(@NonNull String title) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithTitle(title);
        }
    }

    @Override
    public void updateWithDeleteActionShown(boolean shown) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithDeleteActionShown(shown);
        }
    }

    @Override
    public void showDeleteConformationDialog(@NonNull String className) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            final AlertDialog.Builder builder = new AlertDialog.Builder(getView().getViewContext());
            builder.setTitle("Delete");
            builder.setMessage(String.format("Are you sure you want to delete this %s object?", className));
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    interactor.onDeleteObjectConfirmSelected();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    @Override
    public void showSavedSuccessfully() {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().showSavedSuccessfully();
        }
    }
    //endregion
}
