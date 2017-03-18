package de.jonasrottmann.realmbrowser.basemvp;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface BaseView<P extends BasePresenter> {
    void attachPresenter(@Nullable P presenter);
}
