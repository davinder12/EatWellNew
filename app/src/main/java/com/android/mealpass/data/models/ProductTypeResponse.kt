package com.android.mealpass.data.models

data class ProductTypeResponse(
    val body: List<Body>?,
    val status: Status
) {
    data class Body(
        val id: String,
        val product_type: String,
        var isItemSelected: Boolean = false
    )

    data class Status(
        val message: String,
        val success: Boolean
    )
}