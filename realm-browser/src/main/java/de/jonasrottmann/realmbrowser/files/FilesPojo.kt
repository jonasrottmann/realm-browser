package de.jonasrottmann.realmbrowser.files

import android.support.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class FilesPojo internal constructor(val name: String, val sizeInByte: Long)
