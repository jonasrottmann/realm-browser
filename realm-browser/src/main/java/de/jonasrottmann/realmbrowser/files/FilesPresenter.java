package de.jonasrottmann.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;

import de.jonasrottmann.realmbrowser.basemvp.BasePresenterImpl;
import de.jonasrottmann.realmbrowser.files.model.FilesPojo;
import de.jonasrottmann.realmbrowser.helper.RealmHolder;
import de.jonasrottmann.realmbrowser.models.view.ModelsActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FilesPresenter extends BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private final FilesInteractor interactor;

    public FilesPresenter() {
        this.interactor = new FilesInteractor(this);
    }

    @Override
    public void attachView(@NonNull FilesContract.View view) {
        super.attachView(view);
    }

    @Override
    public void requestForContentUpdate(@NonNull Context context) {
        interactor.requestForContentUpdate(context);
    }

    @Override
    public void onFileSelected(FilesPojo item) {
        try {
            RealmConfiguration config = new RealmConfiguration.Builder().name(item.getName()).build();
            RealmHolder.getInstance().setRealmConfiguration(config);
            Realm realm = Realm.getInstance(config);
            realm.close();
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().getViewContext().startActivity(ModelsActivity.getIntent(getView().getViewContext()));
            }
        } catch (RealmMigrationNeededException e) {
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().showToast("RealmMigrationNeededException");
            }
        } catch (Exception e) {
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().showToast("Can't open realm instance. You must close all open realm instances before opening this file.");
            }
        }
    }

    @Override
    public void updateWithFiles(ArrayList<FilesPojo> filesList) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().showFilesList(filesList);
        }
    }
}
