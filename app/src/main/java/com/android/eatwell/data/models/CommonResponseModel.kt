package com.android.eatwell.data.models

data class CommonResponseModel(
    val status: Status
){
    data class Status(
            val code: Int,
            val message: String
    )
}