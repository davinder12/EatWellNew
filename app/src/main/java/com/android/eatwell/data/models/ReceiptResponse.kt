package com.android.eatwell.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ReceiptResponse(
    val body: Body,
    val status: Status
){
    data class Status(
        val code: Int?,
        val count: String,
        val message: String
    )

    data class Body(
        val active_receipts: List<ActiveReceipt>?,
        val used_receipts: List<UsedReceipt>?
    ){

        @Parcelize
        data class ActiveReceipt(
            val address: String,
            val amount: Float,
            val collection_time: String,
            val created_date: String?,
            val currency_type: String,
            val delivery_address: String,
            val delivery_amount: Float,
            val donated_amount: Float? = null,
            val isFromCampaign: Int,
            val is_redeemed: Int,
            val isdelivery: Int,
            val latitude: Double? = null,
            val logo: String,
            val longitude: Double? = null,
                val paymentMethod: String,
                val payment_status: Int,
                val quantity: Int,
                val receipt_id: Int,
                val restaurent_id: Int,
                val storename: String,
                val user_id: Int,
                val username: String,
                var opening_time: String? = "",
                var before_pickup_time: String? = "",
                var closing_time: String? = "",
                var delivery_close_before_hours: String? = "",
                var shop_open_time: String? = "",
                val is_cancel: Int? = 0,
                val isStaffReceipt: Int
        ):Parcelable

        @Parcelize
        data class UsedReceipt(
                val address: String,
                val amount: Float,
                val collection_time: String,
                val created_date: String,
                val currency_type: String,
                val delivery_address: String,
                val delivery_amount: Float,
                val donated_amount: Float,
                val isFromCampaign: Int,
                val is_redeemed: Int,
                val isdelivery: Int,
                val latitude: Double? = null,
                val logo: String,
                val longitude: Double? = null,
                val paymentMethod: String,
                val payment_status: Int,
                val quantity: Int,
                val receipt_id: Int,
                val restaurent_id: Int,
                val storename: String,
                val user_id: Int,
                val username: String,
                val is_cancel: Int? = 0,
                val isStaffReceipt: Int
        ): Parcelable
    }




}