package com.android.mealpass.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProductReceiptResponse(
    val body: Body,
    val status: Status
){
    data class Status(
            val code: Int?,
            val message: String
    )

    @Parcelize
    data class Body(
            val address: String,
            val amount: Float,
            val collection_time: String,
            val countryname: String,
            val created_date: String,
            val currency_type: String,
            val delivery_address: String,
            val delivery_amount: Float,
            val delivery_email: String,
            val donated_amount: Float? = null,
            val email: String,
            val expected_description: String,
            val isFromCampaign: Int,
            val is_redeemed: Int,
            val isdelivery: Int,
            val latitude: Double? = null,
            val logo: String,
            val longitude: Double? = null,
            val mobile: String,
            val name: String,
            val paymentMethod: String,
            val payment_status: Int,
            val quantity: Int,
            val receipt_id: Int,
            val restaurent_id: Int,
            val storename: String,
            var opening_time: String,
            var before_pickup_time: String?,
            var closing_time: String?,
            var delivery_close_before_hours: String?,
            var shop_open_time: String?,
            var isStaffReceipt: Int
    ): Parcelable
}
