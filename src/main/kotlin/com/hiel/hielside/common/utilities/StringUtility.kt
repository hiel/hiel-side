package com.hiel.hielside.common.utilities

const val EMPTY_STRING = ""
const val BLANK_CHAR = " "

fun String.isValidLengthTrimmed(min: Int? = null, max: Int? = null): Boolean {
    return when {
        min != null && max != null -> this.trim().length in min..max
        min != null -> this.trim().length.isGreaterOrEqualThan(min)
        max != null -> this.trim().length.isLessOrEqualThan(max)
        else -> true
    }
}

fun String?.isNotNullValidLengthTrimmed(min: Int? = null, max: Int? = null): Boolean {
    if (this == null) {
        return false
    }
    return this.isValidLengthTrimmed(min, max)
}

fun String?.substringOrNull(startIndex: Int, endIndex: Int? = null): String? {
    return try {
        if (endIndex != null) this!!.substring(startIndex, endIndex) else this!!.substring(startIndex)
    } catch (ex: Exception) {
        null
    }
}

fun String?.ellipsize(length: Int): String? {
    if (isNullOrBlank()) {
        return this
    }
    return this.take(length) + "...".takeIf { this.length > length }
}

fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return String(CharArray(length) { allowedChars.random() })
}
