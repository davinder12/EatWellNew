package com.android.mealpass.data.models




class ActiveReceiptMapper {
    object Mapper {
        fun from(product: ProductReceiptResponse.Body?): ReceiptResponse.Body.ActiveReceipt? {
            return  product?.run {
                   ReceiptResponse.Body.ActiveReceipt(address,amount,
                           collection_time,created_date,currency_type,
                           delivery_address,delivery_amount,donated_amount,
                           isFromCampaign,
                           is_redeemed,isdelivery,latitude,
                           logo,longitude,paymentMethod,payment_status,
                           quantity,receipt_id,restaurent_id,storename,-1,"")
               }

        }
    }
}