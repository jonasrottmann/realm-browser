package de.jonasrottmann.realmbrowser.models

import android.support.annotation.IntDef

object SortMode {
    @IntDef(ASC.toLong(), DESC.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class SortMode

    const val ASC: Int = 0
    const val DESC: Int = 1
}