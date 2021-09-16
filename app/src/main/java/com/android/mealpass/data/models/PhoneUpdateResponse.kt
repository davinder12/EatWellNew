package com.android.mealpass.data.models

data class PhoneUpdateResponse(
    val body: Body,
    val status: Status
) {
    data class Status(
        val code: Int?,
        val is_mobile: Int,
        val is_seller: Int,
        val message: String,
        val user_exist: Int
    )

    data class Body(
        val app_version: String,
        val email: String,
        val mobile: String,
        val name: String,
        val newsletter: String,
        val user_image: String
    )
}