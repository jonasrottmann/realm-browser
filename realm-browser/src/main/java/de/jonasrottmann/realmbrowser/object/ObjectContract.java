package de.jonasrottmann.realmbrowser.object;

import android.content.Context;
import android.support.annotation.RestrictTo;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface ObjectContract {

    interface View extends BaseView<Presenter> {
        Context getViewContext();
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate();
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate();
    }
}