package de.jonasrottmann.realmbrowser.extensions

fun String.toFloatMaybe(): Float? {
    return try {
        toFloat()
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toDoubleMaybe(): Double? {
    return try {
        toDouble()
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toIntMaybe(): Int? {
    return try {
        toInt()
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toLongMaybe(): Long? {
    return try {
        toLong()
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toShortMaybe(): Short? {
    return try {
        toShort()
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toByteMaybe(): Byte? {
    return try {
        toByte()
    } catch (e: NumberFormatException) {
        null
    }
}

