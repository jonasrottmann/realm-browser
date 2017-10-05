package de.jonasrottmann.realmbrowser.extensions

import io.realm.RealmObjectSchema

fun RealmObjectSchema.getPrimaryKeyFieldName(): String? {
    return fieldNames.firstOrNull { isPrimaryKey(it) }
}