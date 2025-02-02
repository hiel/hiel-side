package com.hiel.hielside.common.utilities

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalUnit
import java.util.Date

private val ZONE_ID = ZoneId.of("Asia/Seoul")

enum class DateTimeFormat(val format: String) {
    DATE("yyyyMMdd"),
    DATE_SHORT_YEAR("yyMMdd"),
    DATE_BAR("yyyy-MM-dd"),
    DATETIME("yyyy-MM-dd HH:mm:ss"),
}

const val MINUTE_PER_HOUR = 60
const val SECOND_PER_MINUTE = 60
const val FIRST_MONTH_OF_YEAR = 1
const val LAST_MONTH_OF_YEAR = 12
const val LAST_WEEK_OF_YEAR = 52
const val FIRST_DAY_OF_MONTH = 1
const val LAST_DAY_OF_MONTH = 31

fun OffsetDateTime.toFormatString(format: DateTimeFormat = DateTimeFormat.DATETIME): String =
    this.format(DateTimeFormatter.ofPattern(format.format))

fun String.toOffsetDateTime(format: DateTimeFormat): OffsetDateTime =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(format.format)).atStartOfDay(ZONE_ID).toOffsetDateTime()

fun OffsetDateTime.toDate(): Date = Date.from(this.toInstant())

fun OffsetDateTime.convertToFirstDayOfMonth(): OffsetDateTime = this.withDayOfMonth(FIRST_DAY_OF_MONTH)

fun OffsetDateTime.convertToLastDayOfMonth(): OffsetDateTime = this.withDayOfMonth(this.toLocalDate().lengthOfMonth())

fun OffsetDateTime.initializeTime(): OffsetDateTime =
    this.withHour(0).withMinute(0).withSecond(0).withNano(0)

fun Int.hourToMinute() = (this * MINUTE_PER_HOUR).toLong()

fun Int.hourToSecond() = (this * MINUTE_PER_HOUR * SECOND_PER_MINUTE).toLong()

fun Int.minuteToSecond() = (this * SECOND_PER_MINUTE).toLong()

fun Long.hourToMinute() = this * MINUTE_PER_HOUR

fun Long.hourToSecond() = this * MINUTE_PER_HOUR * SECOND_PER_MINUTE

fun Long.minuteToSecond() = this * SECOND_PER_MINUTE

fun getNowKst(): OffsetDateTime = ZonedDateTime.now(ZONE_ID).toOffsetDateTime()

fun OffsetDateTime.getLastDayOfMonth() = this.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth

fun OffsetDateTime.untilInitializeTime(endExclusive: OffsetDateTime, unit: TemporalUnit)
    = this.initializeTime().until(endExclusive.initializeTime(), unit)
