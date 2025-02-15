package com.android.eatwell.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SaveReceiptRequestModel(
        var storeName: String? = "",
        var amount: Float = 0f,
        val restaurent_id: String? = "",
        val user_id: String? = "",
        val collection_from_time: String? = "",
        val collection_to_time: String? = "",
        val receipt_product_info: List<ReceiptProductInfo>? = null,
        var payment_info: String? = "",
        var delivery_amount: Float = 0f,
        var isDelivery: Boolean = false,
        var currency: String? = "",
        var total_quantity: Int,
        var reference_id: String = "",
        var order_id: String? = "",
        val paymentMethod: String,
        var temp_isSaveCard: Boolean? = false,
        var delivery_address: String? = "",
        var donated_amount: Float = 0f,
        var isFromCampaign: String = "0",
        var isHomeDelivery: String = "0",
        var collection_time: String? = "",
        var isStaffReceipt: Int = 0
) : Parcelable {
    @Parcelize
    data class ReceiptProductInfo(
        val amount: Float = 0f,
        val before_price: String?,
        val price: Float,
        val product_id: String,
        val quantity: Int = 1
    ) : Parcelable
}

