package de.jonasrottmann.realmbrowser.models

import android.support.annotation.RestrictTo
import io.realm.RealmModel

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class InformationPojo(val sizeInByte: Long, val path: String)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class ModelPojo(val klass: Class<out RealmModel>, var count: Long)