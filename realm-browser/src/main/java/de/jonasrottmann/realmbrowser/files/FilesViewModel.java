package de.jonasrottmann.realmbrowser.files;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.RestrictTo;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FilesViewModel extends AndroidViewModel {

    private FilesLiveData data;

    public FilesViewModel(Application application) {
        super(application);
        data = new FilesLiveData(application);
    }

    public FilesLiveData getFiles() {
        return data;
    }

    public void refreshFiles() {
        data.refreshFiles();
    }
}
