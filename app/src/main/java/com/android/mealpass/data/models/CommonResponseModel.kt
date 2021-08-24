package com.android.mealpass.data.models

data class CommonResponseModel(
    val status: Status
){
    data class Status(
            val code: String,
            val message: String
    )
}