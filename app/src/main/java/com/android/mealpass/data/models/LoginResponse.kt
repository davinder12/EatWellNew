package com.android.mealpass.data.models

data class LoginResponse(
    val body: Body,
    val status: Status
) {
    data class Status(
        val code: String,
        val message: String
    )

    data class Body(
        val app_name: String,
        val app_version: String,
        val country: String,
        val created_date: String,
        val device_token: String,
        val device_type: String,
        val email: String,
        val id: String,
        val latitude: String,
        val longitude: String,
        val mobile: String,
        val name: String,
        val newsletter: String,
        val referred: String,
        val secure_key: String,
        val social_id: Any,
        val social_type: Any,
        val status: String,
        val time_zone: Any,
        val user_image: Any,
        val user_type: String
    )
}