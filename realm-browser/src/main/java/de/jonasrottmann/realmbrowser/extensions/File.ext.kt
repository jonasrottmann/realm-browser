package de.jonasrottmann.realmbrowser.extensions

import io.realm.RealmObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.*

fun Field.isNumber(): Boolean {
    return isLong() || isInteger() || isShort() || isByte() || isDouble() || isFloat()
}

fun Field.isLong(): Boolean {
    return type.name == Long::class.java.name || type.name == "long"
}

fun Field.isInteger(): Boolean {
    return type.name == Int::class.java.name || type.name == "int"
}

fun Field.isShort(): Boolean {
    return type.name == Short::class.java.name || type.name == "short"
}

fun Field.isByte(): Boolean {
    return type.name == Byte::class.java.name || type.name == "byte"
}

fun Field.isDouble(): Boolean {
    return type.name == Double::class.java.name || type.name == "double"
}

fun Field.isFloat(): Boolean {
    return type.name == Float::class.java.name || type.name == "float"
}

fun Field.isBoolean(): Boolean {
    return type.name == Boolean::class.java.name || type.name == "boolean"
}

fun Field.isString(): Boolean {
    return type.name == String::class.java.name
}

fun Field.isParametrizedField(): Boolean {
    return genericType is ParameterizedType
}

fun Field.isBlob(): Boolean {
    return type.name == ByteArray::class.java.name
}

fun Field.isDate(): Boolean {
    return type.name == Date::class.java.name
}

fun Field.isRealmObjectField(): Boolean {
    return RealmObject::class.java.isAssignableFrom(type)
}