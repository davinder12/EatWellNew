package com.android.eatwell.data.models

data class TermConditionResponse(
    val body: Body,
    val status: Status
){
    data class Body(
        val terms: String
    )
    data class Status(
        val code: String,
        val message: String
    )
}