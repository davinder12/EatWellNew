package com.android.eatwell.view.units

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*


//yyyy-MM-dd HH:mm:ss

val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s", Locale.ENGLISH)
val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m:s", Locale.ENGLISH)
val localTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
val timeFormatterAmPm: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)


const val DEFAULT_BEFORE_CLOSE_TIME = 30
const val MINUSTES = 60
const val DEFAULT_HOUR = 0


fun dateTime(dateTime: ZonedDateTime?): CharSequence? {
    if (dateTime == null) return null
    return dateTimeFormatter.format(dateTime.toLocalDateTime())

}


fun getAmPmTimeMethod(dateTime: String?):String{
    if(dateTime.isNullOrEmpty()) return ""
    return timeFormatterAmPm.format(LocalDateTime.parse(dateTime, dateTimePattern).atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.systemDefault()))
}

fun convert24HourTo12Hour(dateTime: String?): String? {
    if (dateTime.isNullOrEmpty()) return null
    return timeFormatterAmPm.format(LocalDateTime.parse(dateTime, dateTimePattern).toLocalTime())
}

fun time(locationDateTime: String?): String? {
    if (locationDateTime.isNullOrEmpty()) return null
    return timeFormatter.format(
        LocalDateTime.parse(locationDateTime, dateTimePattern).toLocalTime()
    )
}

fun compareDate(locationDateTime: String?): Boolean {
    if (locationDateTime.isNullOrEmpty()) return false
    val serverDate = LocalDateTime.parse(locationDateTime, dateTimePattern).toLocalDate()
    val localDate = LocalDate.parse(LocalDate.now().format(dateFormatter), dateFormatter)
    return localDate.isEqual(serverDate)
}

fun convertTimeIntoTwoDigit(firstTime: String, secondTime: String): String {
    return LocalTime.parse(firstTime, timeFormatter).toString() + "-" + LocalTime.parse(
        secondTime,
        timeFormatter
    ).toString()
}

fun date(locationDateTime: String?): String? {
    if (locationDateTime == null) return null
    return LocalDateTime.parse(locationDateTime, dateTimePattern).toLocalDate().toString()
}

fun getLocatDate(dateTimeFormat: String?): LocalTime? {
    if (dateTimeFormat == null) return null
    val localTime = LocalDateTime.parse(dateTimeFormat, dateTimePattern).toLocalTime()
    return localTime.minusMinutes(60)
}

fun getCurrentTime(): String = LocalTime.now().format(localTimeFormatter)

fun getCurrentDate(): String = LocalDate.now().format(dateFormatter)


fun canUserCancelOrder(pickUpStartTime: String?, createdDate: String?): Boolean {
    return when {
        pickUpStartTime.isNullOrEmpty() -> false
        else -> {
            val pickUpTime = LocalTime.parse(pickUpStartTime, timeFormatter)
            val currentTime = LocalTime.parse(LocalTime.now().format(localTimeFormatter))
            return currentTime.isBefore(pickUpTime) && compareDate(createdDate)
        }
    }

}


fun isTimeExpired(
        openTime: String?,
        closeTime: String?,
        beforePickTime: String?,
        shopOpenTime: String?/**/
): Boolean {

    return when {
        openTime.isNullOrEmpty() || closeTime.isNullOrEmpty() || beforePickTime.isNullOrEmpty() || shopOpenTime.isNullOrEmpty() -> false
        else -> {
            val startTime = getStartTime(shopOpenTime)
            val endTime = getEndTime(closeTime, beforePickTime)
            val currentTime = LocalTime.parse(LocalTime.now().format(localTimeFormatter))
            currentTime.isAfter(startTime) && currentTime.isBefore(endTime) || currentTime == startTime && currentTime == endTime
        }
    }
}

fun getEndTime(closeTime: String, beforePickTime: String): LocalTime {
    val beforeStartTime = LocalTime.parse(beforePickTime, timeFormatter)
    val endTime = LocalDateTime.parse(closeTime, dateTimePattern).toLocalTime()
    val subtractTime = endTime.minusMinutes(beforeStartTime.minute.toLong())
    return subtractTime.minusHours(beforeStartTime.hour.toLong())

}

fun getStartTime(shopOpenTime: String): LocalTime {
    val shopTime = LocalDateTime.parse(shopOpenTime, dateTimePattern).toLocalTime()
    val defaultTime = LocalTime.parse("00:01:00", timeFormatter)
    return if (shopTime.isAfter(defaultTime)) shopTime else defaultTime
}

//fun getStartTime(shopOpenTime: String, beforePickTime: String): LocalTime {
//    val shopTime = LocalDateTime.parse(shopOpenTime, dateTimePattern).toLocalTime()
//    val defaultTime = LocalTime.parse("00:01:00", timeFormatter)
//    val startTime = if (shopTime.isAfter(defaultTime)) shopTime else defaultTime
//    val beforeStartTime = LocalTime.parse(beforePickTime, timeFormatter)
//    return startTime.minusMinutes(beforeStartTime.minute.toLong())
//        .also { it.minusHours(beforeStartTime.hour.toLong()) }
//}

fun isLocalTimeGreaterThan60Min(localTime: LocalTime): Boolean {
    val totalLocalMinute = (LocalTime.now().hour * MINUSTES) + LocalTime.now().minute
    val notificationTotalMinutes = (localTime.hour * MINUSTES) + localTime.minute
    val remainingMinutes = notificationTotalMinutes - totalLocalMinute
    return remainingMinutes > 0

}


fun isDeliverytimeOnOff(pickUpStartTime: String?, deliveryCloseBeforeTime: String?): Boolean {
    if (pickUpStartTime.isNullOrEmpty()) return false
    var defaultCloseBeforeTime = DEFAULT_BEFORE_CLOSE_TIME
    val currentTime = LocalTime.parse(LocalTime.now().format(localTimeFormatter))
    val startTime = LocalDateTime.parse(pickUpStartTime, dateTimePattern).toLocalTime()
    val totalminutes = ((startTime.hour * MINUSTES) + startTime.minute) - ((currentTime.hour * MINUSTES) + currentTime.minute)

    deliveryCloseBeforeTime?.let {
        val localTime = LocalTime.parse(it, timeFormatter)
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











