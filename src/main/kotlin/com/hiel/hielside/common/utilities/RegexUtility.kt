package com.hiel.hielside.common.utilities

import java.util.regex.Pattern

enum class Regex(val regex: String) {
    EMAIL("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"),
}

fun String.regexMatches(regex: Regex) = Pattern.matches(regex.regex, this)
