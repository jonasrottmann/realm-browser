package de.jonasrottmann.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BaseInteractor;
import de.jonasrottmann.realmbrowser.basemvp.BasePresenter;
import de.jonasrottmann.realmbrowser.basemvp.BaseView;
import de.jonasrottmann.realmbrowser.files.model.FilesPojo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface FilesContract {

    interface View extends BaseView<Presenter> {
        void showFilesList(@NonNull ArrayList<FilesPojo> filesList);

        Context getViewContext();

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate(@NonNull Context context);

        void onFileSelected(FilesPojo item);

        void updateWithFiles(ArrayList<FilesPojo> filesList);
    }


    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@NonNull Context context);
    }
}
