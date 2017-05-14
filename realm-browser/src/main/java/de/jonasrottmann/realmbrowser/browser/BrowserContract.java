package de.jonasrottmann.realmbrowser.browser;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface BrowserContract {

    @IntDef({DisplayMode.REALM_CLASS, DisplayMode.REALM_LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayMode {
        int REALM_CLASS = 0;
        int REALM_LIST = 1;
    }

    interface View extends BaseView<Presenter> {
        Context getViewContext();

        void showMenu();

        void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects);

        void updateWithFABVisibility(boolean visible);

        void updateWithTitle(@NonNull String title);

        void updateWithTextWrap(boolean wrapText);

        void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices);

        void showInformation(long numberOfRows);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @DisplayMode int displayMode);

        void onShowMenuSelected();

        void onFieldSelectionChanged(int fieldIndex, boolean checked);

        void onWrapTextOptionToggled();

        void onNewObjectSelected();

        void onInformationSelected();

        void onAboutSelected();

        void onRowSelected(@NonNull DynamicRealmObject realmObject);

        void showNewObjectActivity(@NonNull final Class<? extends RealmModel> modelClass);

        void showObjectActivity(@NonNull final Class<? extends RealmModel> modelClass);

        void showInformation(long numberOfRows);

        void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects);

        void updateWithFABVisibility(boolean visible);

        void updateWithTitle(@NonNull String title);

        void updateWithTextWrap(boolean wrapText);

        void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices);
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @DisplayMode int displayMode);

        void onWrapTextOptionToggled(@NonNull Context context);

        void onNewObjectSelected();

        void onInformationSelected();

        void onRowSelected(@NonNull DynamicRealmObject realmObject);

        void onFieldSelectionChanged(int fieldIndex, boolean checked);
    }
}