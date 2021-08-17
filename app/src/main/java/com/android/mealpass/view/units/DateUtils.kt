package com.android.mealpass.view.units

import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*


//yyyy-MM-dd HH:mm:ss

val dateTimePattern: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s", Locale.ENGLISH)
val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m", Locale.ENGLISH)
val localTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
val timeFormatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m:s", Locale.ENGLISH)


const val DEFAULT_BEFORE_CLOSE_TIME = 30
const val MINUSTES = 60
const val DEFAULT_HOUR = 0


fun dateTime(dateTime: ZonedDateTime?): CharSequence? {
    if (dateTime == null) return null
    return dateTimeFormatter.format(dateTime.toLocalDateTime())

}


fun time(locationDateTime: String?): String? {
    if (locationDateTime == null) return null
    return timeFormatter.format(
        LocalDateTime.parse(locationDateTime, dateTimePattern).toLocalTime()
    )
}

fun date(locationDateTime: String?): String? {
    if (locationDateTime == null) return null
    return LocalDateTime.parse(locationDateTime, dateTimePattern).toLocalDate().toString()
}

fun getCurrentTime(): String = LocalTime.now().format(localTimeFormatter)


fun isTimeExpired(
    openTime: String?,
    closeTime: String?,
    beforePickTime: String?,
    shopOpenTime: String?
): Boolean {
    return when {
        openTime.isNullOrEmpty() || closeTime.isNullOrEmpty() || beforePickTime.isNullOrEmpty() || shopOpenTime.isNullOrEmpty() -> false
        else -> {
            val startTime = getStartTime(shopOpenTime, beforePickTime)
            val endTime = LocalDateTime.parse(closeTime, dateTimePattern).toLocalTime()
            val currentTime = LocalTime.parse(LocalTime.now().format(localTimeFormatter))
            currentTime.isAfter(startTime) && currentTime.isBefore(endTime) || currentTime == startTime && currentTime == endTime
        }
    }
}

fun getStartTime(shopOpenTime: String, beforePickTime: String): LocalTime {
    val shopTime = LocalDateTime.parse(shopOpenTime, dateTimePattern).toLocalTime()
    val defaultTime = LocalTime.parse("00:01", timeFormatter)
    val startTime = if (shopTime.isAfter(defaultTime)) shopTime else defaultTime
    val beforeStartTime = LocalTime.parse(beforePickTime, timeFormatter)
    return startTime.minusMinutes(beforeStartTime.minute.toLong())
        .also { it.minusHours(beforeStartTime.hour.toLong()) }
}


fun isDeliverytimeOnOff(pickUpStartTime: String?, deliveryCloseBeforeTime: String?): Boolean {
    if (pickUpStartTime.isNullOrEmpty()) return false
    var defaultCloseBeforeTime = DEFAULT_BEFORE_CLOSE_TIME
    val currentTime = LocalTime.parse(LocalTime.now().format(timeFormatter))
    val startTime = LocalDateTime.parse(pickUpStartTime, dateTimePattern).toLocalTime()
    val totalminutes =
        ((startTime.hour * MINUSTES) + startTime.minute) - ((currentTime.hour * MINUSTES) + currentTime.minute)

    deliveryCloseBeforeTime?.let {
        val localTime = LocalTime.parse(it, timeFormatter2)
        defaultCloseBeforeTime = when {
            localTime.hour > DEFAULT_HOUR -> localTime.hour * MINUSTES + localTime.minute
            localTime.minute > defaultCloseBeforeTime -> localTime.minute
            else -> {
                defaultCloseBeforeTime
            }
        }
    }

    return totalminutes >= defaultCloseBeforeTime

}











