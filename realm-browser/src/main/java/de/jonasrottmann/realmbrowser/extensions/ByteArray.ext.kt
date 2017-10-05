package de.jonasrottmann.realmbrowser.extensions


val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

fun ByteArray.toStringHex(): String {
    val hexChars = CharArray(size * 2)
    for ((index, byte) in this.withIndex()) {
        val ubyte = (byte.toInt() and 0xFF)
        hexChars[index * 2] = hexArray[ubyte ushr 4]
        hexChars[index * 2 + 1] = hexArray[ubyte and 0x0F]
    }
    return String(hexChars)
}


fun ByteArray.createBlobValueString(limit: Int): String? {
    val builder = StringBuilder("byte[] = ")
    builder.append("{")
    for (i in 0 until if (limit == 0) size else Math.min(size, limit)) {
        builder.append(get(i).toString())
        if (i < size - 1) {
            builder.append(", ")
        }
    }
    if (limit != 0) builder.append("...")
    builder.append("}")
    return builder.toString()
}