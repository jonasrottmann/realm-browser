package de.jonasrottmann.realmbrowser.basemvp;

import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public abstract class BaseInteractorImpl<P extends BasePresenter> implements BaseInteractor {
    private final P presenter;

    protected P getPresenter() {
        return presenter;
    }

    protected BaseInteractorImpl(P presenter) {
        this.presenter = presenter;
    }
}
