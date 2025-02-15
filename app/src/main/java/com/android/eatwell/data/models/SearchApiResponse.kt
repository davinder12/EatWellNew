package com.android.eatwell.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize

data class SearchApiResponse(
    val body: List<Body>,
    val status: Status

) {
    data class Status(
        val message: String,
        val notification_Count: Int,
        val success: Boolean,
        val total_count: String
    )

    data class Body(
        var offset: Int,
        val merchant_info: MerchantInfo,
        val product_info: List<ProductInfo>
    ) :
        ClusterItem {

        override fun getTitle(): String? {
            return merchant_info.storename
        }

        override fun getPosition(): LatLng {
            return LatLng(merchant_info.latitude, merchant_info.longitude)
        }

        override fun getSnippet(): String? {
            return ""
        }

        data class MerchantInfo(
            val before_pickup_time: String,
            val currency_type: String,
            val delivery_amount: String,
            val distance: String,
            val favourite_count: String,
            val food_category: String,
            val isCurrentLike: Boolean,
            val is_home_delivery: Boolean,
            val is_open: Boolean,
            val latitude: Double,
            val logo: String,
            val longitude: Double,
            val merchant_donation_only: Boolean,
            val name: String,
            val restaurent_id: String,
            val shop_open_time: String,
            val sponsor_portion: Int,
            val storename: String
        )

        @Parcelize
        data class ProductInfo(
            val before_price: String,
            val campaign_itemleft: Int,
            val currency_type: String,
            val discounted_from_time: String,
            val discounted_price: Float,
            val discounted_to_time: String,
            val expected_description: String,
            val itemleft: Int,
            val logo: String,
            val name: String,
            val offer_message: String,
            val pickup_close_time: String,
            val pickup_start_time: String,
            val portion: Int,
            val price: String,
            val product_donation_only: Boolean,
            val product_id: String,
            val product_types: List<ProductType>,
            var resturantId: String = "",
            var address: String = "",
            var resturantName: String = ""
        ) : Parcelable {
            @Parcelize
            data class ProductType(
                val id: String,
                val product_type: String
            ) : Parcelable
        }
    }

}