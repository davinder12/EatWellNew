package com.android.mealpass.data.models

data class SignUpRequestModel(
    val version_name: String,
    val device_token: String?,
    val device_type: String,
    val email: String?,
    val mobile: String?,
    val name: String?,
    val newsletter: Boolean,
    val password: String?
)