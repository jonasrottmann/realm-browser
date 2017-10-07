package de.jonasrottmann.realmbrowser.models

import android.arch.lifecycle.LiveData
import android.support.annotation.RestrictTo
import android.support.annotation.UiThread
import android.text.TextUtils
import de.jonasrottmann.realmbrowser.ioThread
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsLiveData(private val realm: Realm, sortMode: Int, filter: String) : LiveData<List<ModelPojo>>() {

    @SortMode.SortMode
    var sortMode: Int = SortMode.ASC
        set(value) {
            field = value
            updateValueAsync()
        }
    var filter: String = ""
        set(value) {
            field = value.trim()
            updateValueAsync()
        }
    private var unfiltered: List<ModelPojo> = ArrayList()

    init {
        this.sortMode = sortMode
        this.filter = filter
        loadModels()
    }

    private fun sortPojos(pojos: List<ModelPojo>, @SortMode.SortMode sortMode: Int): List<ModelPojo> {
        Collections.sort(pojos) { o1, o2 -> o1.klass.simpleName.compareTo(o2.klass.simpleName) }
        if (sortMode == SortMode.DESC) {
            Collections.reverse(pojos)
        }
        return pojos
    }

    private fun filterPojos(pojos: List<ModelPojo>, filter: String): List<ModelPojo> {
        var filteredPojos: ArrayList<ModelPojo>? = null
        if (TextUtils.isGraphic(filter)) {
            filteredPojos = pojos.filterTo(ArrayList()) { it.klass.simpleName.toLowerCase().contains(filter.toLowerCase()) }
        }
        return if (filteredPojos == null) pojos else filteredPojos
    }

    @UiThread
    fun loadModels() {
        val realmModelClasses = ArrayList(realm.configuration.realmObjectClasses)
        val pojos = realmModelClasses.map { ModelPojo(it, realm.where(it).findAll().size.toLong()) }
        this.unfiltered = pojos
        updateValueAsync()
    }

    private fun updateValueAsync() {
        ioThread {
            postValue(filterPojos(sortPojos(unfiltered, this@ModelsLiveData.sortMode), this@ModelsLiveData.filter))
        }
    }
}
