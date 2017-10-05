package de.jonasrottmann.realmbrowser.files

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.FileObserver
import android.support.annotation.RestrictTo
import de.jonasrottmann.realmbrowser.ioThread
import java.io.File
import java.util.*

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesLiveData(context: Context) : LiveData<List<FilesPojo>>() {
    private val fileObserver: FileObserver
    private val dataDir: String = context.applicationInfo.dataDir

    init {
        this.fileObserver = object : FileObserver(dataDir, MODIFY or CREATE or DELETE or MOVED_FROM or MOVED_TO) {
            override fun onEvent(event: Int, path: String?) = loadFiles()
        }
        loadFiles()
    }

    override fun onActive() = fileObserver.startWatching()

    override fun onInactive() = fileObserver.stopWatching()

    fun loadFiles() {
        ioThread {
            postValue(File(this@FilesLiveData.dataDir, "files").listFiles().toList()
                    .filter { isValidFileName(it.name) }
                    .map { FilesPojo(it.name, it.length()) })
        }
    }

    companion object RealmFileValidator {
        private val ignoreExtensionList = ArrayList<String>()

        init {
            ignoreExtensionList.add(".log")
            ignoreExtensionList.add(".log_a")
            ignoreExtensionList.add(".log_b")
            ignoreExtensionList.add(".lock")
            ignoreExtensionList.add(".management")
            ignoreExtensionList.add(".temp")
        }

        fun isValidFileName(fileName: String): Boolean = fileName.lastIndexOf(".") > 0 && !ignoreExtensionList.contains(fileName.substring(fileName.lastIndexOf(".")))
    }
}
