package com.android.mealpass.view.units

import android.content.Context
import android.util.Log
import mealpass.com.mealpass.R
import kotlin.math.roundToInt


private const val PORTION_MAX_VALUE = 5
private const val PORTION_MIN_VALUE = 1
private const val PORTION_STATIC_VALUE = "5 +"
private const val RESTURANT_OPEN = 1
private const val RESTURANT_ACTIVE = 1
private const val HOME_DELIVERY_ON = 1


fun isHomeDeliveryAvailable(
    isHomeDelivery: Int,
    isFullDayDeliveryAllowed: Boolean,
    openTime: String?,
    deliveryCloseBeforeHours: String?
): Boolean {
    return isFullDayDeliveryAllowed || isHomeDelivery == HOME_DELIVERY_ON && isDeliverytimeOnOff(
        openTime, deliveryCloseBeforeHours
    )
}

fun pickUpTime(startingDateTime: String?, endDateTime: String?): String {
    var time = ""
    time(startingDateTime)?.let { time = it }
    time(endDateTime)?.let { time = "$time-$it" }
    return time
}

fun getPercentage(discountedPrice: Float, actualPrice: Float): Int {
    var percentage = 0
    try {
        val savingAmount = discountedPrice.roundToInt() - actualPrice.roundToInt()
        percentage = (savingAmount / discountedPrice.roundToInt()) * 100
        return percentage
    } catch (e: Exception) {
    }
    return percentage
}

fun getPortion(portion: Int, context: Context): String {
    return when {
        portion > PORTION_MAX_VALUE -> PORTION_STATIC_VALUE + context.getString(R.string.left)
        portion in PORTION_MIN_VALUE..PORTION_MAX_VALUE -> "$portion " + context.getString(R.string.left)
        else -> context.getString(R.string.OutOfStock)

    }
}


fun getTime(date: String?): String {
    var time = ""
    try {
        time(date)?.let { time = it }
    } catch (e: Exception) {
        Log.e("Time format", "time format")
    }
    return time
}

fun getDate(date: String?): String {
    var dateTime = ""
    try {
        date(date)?.let { dateTime = it }
    } catch (e: Exception) {
        Log.e("Date format", "date format")
    }
    return dateTime
}

// open time show pickup start time
// close time show pickup close time
// beforePickup time use for merchant to close resturant/product before open/start time
// shopOpenTime : represent open time if we got any time other wise open time use as it is

fun isResturantOpen(
    qty: Int, isResturantOpen: Int, openTime: String?,
    closeTime: String?, beforePickTime: String?, shopOpenTime: String?, isActive: Int
): Boolean {

    Log.e("open time", "" + openTime)
    Log.e("close time", "" + closeTime)
    Log.e("beforePickTime time", "" + beforePickTime)
    Log.e("shop Open time", "" + shopOpenTime)

    return when {
        qty <= 0 -> false
        else -> try {
            isActive == RESTURANT_ACTIVE && isResturantOpen == RESTURANT_OPEN && isTimeExpired(
                openTime,
                closeTime,
                beforePickTime,
                shopOpenTime
            )
        } catch (e: Exception) {
            Log.e("number format exception", "" + e)
            false
        }
    }
}
















