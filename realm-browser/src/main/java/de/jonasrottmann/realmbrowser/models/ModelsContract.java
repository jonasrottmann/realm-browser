package de.jonasrottmann.realmbrowser.models;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;
import de.jonasrottmann.realmbrowser.models.model.ModelPojo;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface ModelsContract {
    @Retention(SOURCE)
    @IntDef({SortMode.ASC, SortMode.DESC})
    @interface SortMode {
        int ASC = 0;
        int DESC = 1;
    }

    interface View extends BaseView<Presenter> {
        void updateWithModels(@NonNull ArrayList<ModelPojo> filesList, @SortMode int sortMode);

        Context getViewContext();
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate();

        void onModelSelected(ModelPojo item);

        void onSortModeChanged();

        void onFilterChanged(@NonNull String filter);

        void updateWithModels(@NonNull ArrayList<ModelPojo> modelsList, @SortMode int sortMode);
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate();

        void updateWithFilter(String filter);

        void updateWithSortModeChanged();
    }
}