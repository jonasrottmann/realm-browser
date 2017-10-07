package de.jonasrottmann.realmbrowser.files

import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v7.util.DiffUtil

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class FilesDiffUtilsCallback(private val mOldList: List<FilesPojo>, private val mNewList: List<FilesPojo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size
    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition].name == mOldList[oldItemPosition].name
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = mNewList[newItemPosition] == mOldList[oldItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val (nameNew, sizeInByteNew) = mNewList[newItemPosition]
        val (nameOld, sizeInByteOld) = mOldList[oldItemPosition]

        val diffBundle = Bundle()
        if (nameNew != nameOld) diffBundle.putString(KEY_NAME, nameNew)
        if (sizeInByteNew != sizeInByteOld) diffBundle.putLong(KEY_SIZE, sizeInByteNew)

        return if (diffBundle.size() == 0) null else diffBundle
    }

    companion object {
        val KEY_NAME = "KEY_NAME"
        val KEY_SIZE = "KEY_SIZE"
    }
}
