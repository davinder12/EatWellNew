package com.android.mealpass.data.models

data class NotificationResponse(
    val body: List<Body>?,
    val status: Status
){

    data class Status(
            val code: Int?,
            val message: String
    )

    data class Body(
            val created_at: String,
            val id: String,
            val merchant_id: String,
            val merchant_logo: String,
            val merchant_name: String,
            val message: String,
            val notification_type: Int?
    )
}