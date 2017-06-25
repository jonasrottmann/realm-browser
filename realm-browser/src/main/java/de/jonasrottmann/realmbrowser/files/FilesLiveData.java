package de.jonasrottmann.realmbrowser.files;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.RestrictTo;
import android.text.format.Formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static de.jonasrottmann.realmbrowser.files.RealmFileValidator.isValidFileName;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesLiveData extends LiveData<List<FilesPojo>> {

    private final Context context;

    FilesLiveData(Context context) {
        this.context = context;
        loadFiles();
    }

    void refreshFiles() {
        loadFiles();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadFiles() {
        /*
         * AsyncTask here is safe.
         * See https://medium.com/google-developers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4
         */
        new AsyncTask<Void, Void, List<FilesPojo>>() {
            @Override
            protected List<FilesPojo> doInBackground(Void... voids) {
                File dataDir = new File(context.getApplicationInfo().dataDir, "files");
                File[] filesArray = dataDir.listFiles();
                ArrayList<FilesPojo> fileList = new ArrayList<>();
                if (filesArray != null) {
                    for (File file : filesArray) {
                        String fileName = file.getName();
                        if (isValidFileName(fileName)) {
                            fileList.add(new FilesPojo(fileName, Formatter.formatShortFileSize(context, file.length()), file.length()));
                        }
                    }
                }
                return fileList;
            }

            @Override
            protected void onPostExecute(List<FilesPojo> filesPojos) {
                setValue(filesPojos);
            }
        }.execute();
    }
}
