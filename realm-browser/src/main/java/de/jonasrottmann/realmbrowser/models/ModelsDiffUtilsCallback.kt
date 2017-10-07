package de.jonasrottmann.realmbrowser.models

import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v7.util.DiffUtil

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class ModelsDiffUtilsCallback(private val mOldList: List<ModelPojo>, private val mNewList: List<ModelPojo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size
    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition].klass == mOldList[oldItemPosition].klass
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition] == mOldList[oldItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val (klassNew, countNew) = mNewList[newItemPosition]
        val (klassOld, countOld) = mOldList[oldItemPosition]

        val diffBundle = Bundle()
        if (klassNew != klassOld) diffBundle.putSerializable(KEY_CLASS, klassNew)
        if (countNew != countOld) diffBundle.putLong(KEY_COUNT, countNew)

        return if (diffBundle.size() == 0) null else diffBundle
    }

    companion object {
        val KEY_CLASS = "KEY_CLASS"
        val KEY_COUNT = "KEY_COUNT"
    }
}
