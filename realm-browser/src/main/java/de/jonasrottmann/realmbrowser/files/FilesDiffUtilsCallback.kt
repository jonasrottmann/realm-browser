package de.jonasrottmann.realmbrowser.files

import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v7.util.DiffUtil
import timber.log.Timber

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class FilesDiffUtilsCallback(private val mOldList: List<FilesPojo>, private val mNewList: List<FilesPojo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size ?: 0
    override fun getNewListSize(): Int = mNewList.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition].name == mOldList[oldItemPosition].name
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition] == mOldList[oldItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        Timber.d("getChangePayload")

        val newFile = mNewList[newItemPosition]
        val oldFile = mOldList[oldItemPosition]

        val diffBundle = Bundle()
        if (newFile.name != oldFile.name) diffBundle.putString(KEY_NAME, newFile.name)
        if (newFile.sizeInByte != oldFile.sizeInByte) diffBundle.putLong(KEY_SIZE, newFile.sizeInByte)

        return if (diffBundle.size() == 0) null else diffBundle
    }

    companion object {
        val KEY_NAME = "KEY_NAME"
        val KEY_SIZE = "KEY_SIZE"
    }
}
