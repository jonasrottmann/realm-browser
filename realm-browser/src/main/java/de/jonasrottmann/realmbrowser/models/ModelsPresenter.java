package de.jonasrottmann.realmbrowser.models;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BasePresenterImpl;
import de.jonasrottmann.realmbrowser.browser.BrowserContract;
import de.jonasrottmann.realmbrowser.browser.view.RealmBrowserActivity;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.models.model.InformationPojo;
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
    public void onInformationSelected() {
        interactor.onInformationSelected();
    }

    @Override
    public void onModelSelected(ModelPojo item) {
        if (isViewAttached()) {
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CLASS, item.getKlass());
            //noinspection ConstantConditions
            RealmBrowserActivity.start(getView().getViewContext(), BrowserContract.DisplayMode.REALM_CLASS);
        }
    }

    @Override
    public void onShareSelected() {
        interactor.onShareSelected();
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

    @Override
    public void presentShareDialog(@NonNull String path) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().presentShareDialog(path);
        }
    }

    @Override
    public void showInformation(@NonNull InformationPojo informationPojo) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().showInformation(informationPojo);
        }
    }
    //endregion
}
