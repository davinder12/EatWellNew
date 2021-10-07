package com.android.mealpass.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MerchantNotificationResponse(
    val body: Body,
    val status: Status
){
    data class Status(
            val code: Int?,
            val message: String,
            val total_count: Int?,
            val unseen_count: Int?
    )


    @Parcelize
    data class Body(
            var cost_price: Float,
            val currency_type: String,
            val expected_description: String,
            var is_open: Int?,
            val notifictions: List<Notifiction>?,
            val offer_message: String,
            var protion: Int,
            val protion_price: Float,
            var retail_price: Float,
            val sold_portion: Int
    ): Parcelable {

        @Parcelize
        data class Notifiction(
                val amount: Float,
                val collection_time: String,
                val created_date: String,
                val currency_type: String,
                val delivery_address: String,
                val delivery_amount: Float?,
                val description: String?,
                val is_redeemed: Int?,
                val isdelivery: Int?,
                val name: String,
                val quantity: String?,
                val receipt_id: String?,
                val storename: String,
                val is_cancel: Int?,
                val isStaffReceipt: Int
        ) : Parcelable
    }




}