package com.hiel.hielside.common.utilities

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

private val ZONE_ID = ZoneId.of("Asia/Seoul")

const val MINUTE_PER_HOUR = 60
const val SECOND_PER_MINUTE = 60
const val FIRST_MONTH_OF_YEAR = 1
const val LAST_MONTH_OF_YEAR = 12
const val LAST_WEEK_OF_YEAR = 52

fun OffsetDateTime.toFormatString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}

fun String.toOffsetDateTime(format: String): OffsetDateTime {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(format)).atStartOfDay(ZONE_ID).toOffsetDateTime()
}

fun OffsetDateTime.toDate(): Date = Date.from(this.toInstant())

fun Int.hourToMinute() = (this * MINUTE_PER_HOUR).toLong()

fun Int.hourToSecond() = (this * MINUTE_PER_HOUR * SECOND_PER_MINUTE).toLong()

fun Int.minuteToSecond() = (this * SECOND_PER_MINUTE).toLong()

fun Long.hourToMinute() = this * MINUTE_PER_HOUR

fun Long.hourToSecond() = this * MINUTE_PER_HOUR * SECOND_PER_MINUTE

fun Long.minuteToSecond() = this * SECOND_PER_MINUTE

fun getNowKst(): OffsetDateTime = ZonedDateTime.now(ZONE_ID).toOffsetDateTime()
