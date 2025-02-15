package com.android.eatwell.data.models

data class FoodRequestModel(
    val current_date_time: String? = "",
    val latitude: String? = "",
    var limit: Int,
    val longitude: String? = "",
    var offset: Int,
    val time_zone: String = "",
    val user_id: String? = "",
    val keyword: String? = "",
    val countryName: String? = "",
    val category: String? = "",
    val showOpenResturantFood: String? = "",
    val toTime: String? = "",
    val fromTime: String? = "",
    val productFilterType: List<String>? = listOf(),
    val defaultLimit:Int = 5,
    val perviousHoldOffset:Int =0

)



