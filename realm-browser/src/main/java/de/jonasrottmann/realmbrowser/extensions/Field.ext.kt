@file:JvmName("FieldUtils")

package de.jonasrottmann.realmbrowser.extensions

import io.realm.RealmObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.*

fun Field.isNumber(): Boolean {
    return isLong() || isInteger() || isShort() || isByte() || isDouble() || isFloat()
}

fun Field.isLong(): Boolean {
    return type.simpleName == Long::class.java.simpleName || type.simpleName == "long" || type.simpleName == "Long"
}

fun Field.isInteger(): Boolean {
    return type.simpleName == Int::class.java.simpleName || type.simpleName == "int" || type.simpleName == "Int" || type.simpleName == "Integer" || type.simpleName == "integer"
}

fun Field.isShort(): Boolean {
    return type.simpleName == Short::class.java.simpleName || type.simpleName == "short" || type.simpleName == "Short"
}

fun Field.isByte(): Boolean {
    return type.simpleName == Byte::class.java.simpleName || type.simpleName == "byte" || type.simpleName == "Byte"
}

fun Field.isDouble(): Boolean {
    return type.simpleName == Double::class.java.simpleName || type.simpleName == "double" || type.simpleName == "Double"
}

fun Field.isFloat(): Boolean {
    return type.simpleName == Float::class.java.simpleName || type.simpleName == "float" || type.simpleName == "Float"
}

fun Field.isBoolean(): Boolean {
    return type.simpleName == Boolean::class.java.simpleName || type.simpleName == "boolean" || type.simpleName == "Boolean"
}

fun Field.isString(): Boolean {
    return type.simpleName == String::class.java.simpleName
}

fun Field.isParametrizedField(): Boolean {
    return genericType is ParameterizedType
}

fun Field.isBlob(): Boolean {
    return type.simpleName == ByteArray::class.java.simpleName
}

fun Field.isDate(): Boolean {
    return type.simpleName == Date::class.java.simpleName
}

fun Field.isRealmObjectField(): Boolean {
    return RealmObject::class.java.isAssignableFrom(type)
}