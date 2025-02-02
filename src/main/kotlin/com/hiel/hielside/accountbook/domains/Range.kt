package com.hiel.hielside.accountbook.domains

data class Range<T>(
    val start: T,
    val end: T,
)

fun <T> Pair<T, T>.toRange() = Range(start = first, end = second)
