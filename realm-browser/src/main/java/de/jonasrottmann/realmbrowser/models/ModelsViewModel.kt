package de.jonasrottmann.realmbrowser.models

import android.arch.lifecycle.ViewModel
import android.support.annotation.RestrictTo
import de.jonasrottmann.realmbrowser.helper.DataHolder
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsViewModel : ViewModel() {

    private val realm: Realm
    private val config: RealmConfiguration? = DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG) as RealmConfiguration?
    val data: ModelsLiveData

    val sortMode: Int
        @SortMode.SortMode get() = data.sortMode
    val filter: String
        get() = data.filter
    val information: InformationPojo?
        get() {
            if (config == null) return null
            val realmFile = File(config.path)
            var sizeInByte: Long = 0
            if (realmFile.exists() && !realmFile.isDirectory) {
                sizeInByte = realmFile.length()
            }
            return InformationPojo(sizeInByte, realmFile.path)
        }

    init {
        this.realm = Realm.getInstance(config!!)
        this.data = ModelsLiveData(realm, SortMode.ASC, "")
    }

    fun refreshData() = data.loadModels()

    fun changeSortMode() {
        data.sortMode = (data.sortMode + 1) % 2
    }

    fun changeFilter(filter: String) {
        data.filter = filter
    }

    override fun onCleared() {
        realm.close()
    }
}
