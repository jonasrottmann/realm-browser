package de.jonasrottmann.realmbrowser.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

import de.jonasrottmann.realmbrowser.R;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenterImpl;
import de.jonasrottmann.realmbrowser.helper.DataHolder;
import de.jonasrottmann.realmbrowser.object.view.RealmObjectActivity;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BrowserPresenter extends BasePresenterImpl<BrowserContract.View> implements BrowserContract.Presenter {
    private final BrowserInteractor interactor;

    public BrowserPresenter() {
        interactor = new BrowserInteractor(this);
    }

    //region ViewOutput
    @Override
    public void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @BrowserContract.DisplayMode int displayMode, boolean selectionMode) {
        interactor.requestForContentUpdate(context, dynamicRealm, displayMode, selectionMode);
    }

    @Override
    public void onShowMenuSelected() {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().showMenu();
        }
    }

    @Override
    public void onFieldSelectionChanged(int fieldIndex, boolean checked) {
        interactor.onFieldSelectionChanged(fieldIndex, checked);
    }

    @Override
    public void onWrapTextOptionToggled() {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            interactor.onWrapTextOptionToggled(getView().getViewContext());
        }
    }

    @Override
    public void onNewObjectSelected() {
        interactor.onNewObjectSelected();
    }

    @Override
    public void onInformationSelected() {
        interactor.onInformationSelected();
    }

    @Override
    public void onAboutSelected() {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().getViewContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getView().getViewContext().getString(R.string.realm_browser_git))));
        }
    }

    @Override
    public void onRowSelected(@NonNull DynamicRealmObject realmObject) {
        interactor.onRowSelected(realmObject);
    }
    //endregion

    //region InteractorOutput
    @Override
    public void showNewObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        if (isViewAttached()) {
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CLASS, modelClass);
            //noinspection ConstantConditions
            getView().getViewContext().startActivity(RealmObjectActivity.getIntent(getView().getViewContext(), true));
        }
    }

    @Override
    public void showObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().getViewContext().startActivity(RealmObjectActivity.getIntent(getView().getViewContext(), false));
        }
    }

    @Override
    public void showInformation(long numberOfRows) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().showInformation(numberOfRows);
        }
    }

    @Override
    public void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithRealmObjects(objects);
        }
    }

    @Override
    public void updateWithFABVisibility(boolean visible) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithFABVisibility(visible);
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
    public void updateWithTextWrap(boolean wrapText) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithTextWrap(wrapText);
        }
    }

    @Override
    public void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithFieldList(fields, selectedFieldIndices);
        }
    }

    @Override
    public void closeActivityForSelectionResult() {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().closeActivityForSelectionResult();
        }
    }
    //endregion
}
