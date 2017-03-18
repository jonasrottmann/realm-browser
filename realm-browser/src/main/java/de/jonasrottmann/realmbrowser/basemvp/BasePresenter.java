package de.jonasrottmann.realmbrowser.basemvp;

import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}
