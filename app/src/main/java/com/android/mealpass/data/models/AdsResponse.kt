package com.android.mealpass.data.models

data class AdsResponse(
    val body: List<Body>?,
    val status: Status
){
    data class Body(
        val address: String,
        val country: String,
        val description: String?,
        val email: String,
        val id: String,
        val image_url: String,
        val link: String,
        val name: String,
        val organization_name: String,
        val phone: String,
        val picture_url: String
    )

    data class Status(
        val code: Int?,
        val message: String
    )
}