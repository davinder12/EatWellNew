package com.android.mealpass.data.models

data class GetAllResturantRequest(
    val filter_product_type: List<String>?,
    val food_category: String,
    val latitude: String?,
    val longitude: String?,
    val time_zone: String,
    val user_id: String,
    val showOpenResturantFood: String?,
    val toTime: String?,
    val fromTime: String?,
    val currentTime: String?
)