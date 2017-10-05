package de.jonasrottmann.realmbrowser.files

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.support.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesViewModel(application: Application) : AndroidViewModel(application) {

    val files: FilesLiveData = FilesLiveData(application)

    fun refreshFiles() {
        files.loadFiles()
    }
}
