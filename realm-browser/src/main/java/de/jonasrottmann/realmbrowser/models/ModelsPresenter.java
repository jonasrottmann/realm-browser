package de.jonasrottmann.realmbrowser.models;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BasePresenterImpl;
import de.jonasrottmann.realmbrowser.browser.view.RealmBrowserActivity;
import de.jonasrottmann.realmbrowser.models.model.ModelPojo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ModelsPresenter extends BasePresenterImpl<ModelsContract.View> implements ModelsContract.Presenter {
    private final ModelsInteractor interactor;

    public ModelsPresenter() {
        interactor = new ModelsInteractor(this);
    }

    //region ViewOutput
    @Override
    public void onSortModeChanged() {
        interactor.updateWithSortModeChanged();
    }

    @Override
    public void requestForContentUpdate() {
        interactor.requestForContentUpdate();
    }

    @Override
    public void onFilterChanged(@NonNull String filter) {
        interactor.updateWithFilter(filter);
    }

    @Override
    public void onModelSelected(ModelPojo item) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            RealmBrowserActivity.start(getView().getViewContext(), item.getKlass());
        }
    }
    //endregion

    //region InteractorOutput
    @Override
    public void updateWithModels(@NonNull ArrayList<ModelPojo> modelsList, @ModelsContract.SortMode int sortMode) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithModels(modelsList, sortMode);
        }
    }
    //endregion
}
