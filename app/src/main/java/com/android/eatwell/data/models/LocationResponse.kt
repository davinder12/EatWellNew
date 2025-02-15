package com.android.eatwell.data.models

data class LocationResponse(
    val body: Body,
    val status: Status
){
    data class Body(
        val allowDailyPush: Boolean? =false,
        val daily_message: String? = "" ,
        val notification_count: Int
    )

    data class Status(
        val code: Int?,
        val message: String
    )
}
