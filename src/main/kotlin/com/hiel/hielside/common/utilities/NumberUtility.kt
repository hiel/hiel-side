package com.hiel.hielside.common.utilities

fun Long.isPositive(): Boolean = this > 0

fun Long.isPositiveOrZero(): Boolean = this >= 0

fun Long.isNegative(): Boolean = this < 0

fun Long.isNegativeOrZero(): Boolean = this <= 0

fun Long.isZero(): Boolean = this == 0L

fun Int.isPositive(): Boolean = this > 0

fun Int.isPositiveOrZero(): Boolean = this >= 0

fun Int.isNegative(): Boolean = this < 0

fun Int.isNegativeOrZero(): Boolean = this <= 0

fun Int.isZero(): Boolean = this == 0

fun Long.isGreaterThan(other: Long): Boolean = this > other

fun Long.isGreaterThan(other: Int): Boolean = this > other

fun Long.isGreaterThan(other: String): Boolean = this > other.toLong()

fun Int.isGreaterThan(other: Int): Boolean = this > other

fun Int.isGreaterThan(other: Long): Boolean = this > other

fun Int.isGreaterThan(other: String): Boolean = this > other.toInt()

fun Long.isGreaterOrEqualThan(other: Long): Boolean = this >= other

fun Long.isGreaterOrEqualThan(other: Int): Boolean = this >= other

fun Long.isGreaterOrEqualThan(other: String): Boolean = this >= other.toLong()

fun Int.isGreaterOrEqualThan(other: Int): Boolean = this >= other

fun Int.isGreaterOrEqualThan(other: Long): Boolean = this >= other

fun Int.isGreaterOrEqualThan(other: String): Boolean = this >= other.toInt()

fun Long.isLessThan(other: Long): Boolean = this < other

fun Long.isLessThan(other: Int): Boolean = this < other

fun Long.isLessThan(other: String): Boolean = this < other.toLong()

fun Int.isLessThan(other: Int): Boolean = this < other

fun Int.isLessThan(other: Long): Boolean = this < other

fun Int.isLessThan(other: String): Boolean = this < other.toInt()

fun Long.isLessOrEqualThan(other: Long): Boolean = this <= other

fun Long.isLessOrEqualThan(other: Int): Boolean = this <= other

fun Long.isLessOrEqualThan(other: String): Boolean = this <= other.toLong()

fun Int.isLessOrEqualThan(other: Int): Boolean = this <= other

fun Int.isLessOrEqualThan(other: Long): Boolean = this <= other

fun Int.isLessOrEqualThan(other: String): Boolean = this <= other.toInt()

fun Int.zeroFill(length: Int): String = this.toString().padStart(length, '0')
