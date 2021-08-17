package com.android.mealpass.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class FoodData(
    val body: List<Body>,
    val status: Status
) {

    data class Status(
        val code: String,
        val message: String,
        val notification_Count: Int,
        val total_count: String
    )

    data class Body(
        val address: String?,
        val allow_fullday_delivery: Boolean,
        val allow_subscription: String?,
        val alternate_email: String?,
        val before_pickup_time: String?,
        val before_price: String?,
        val campaign_itemleft: Int,
        val city: String?,
        val closing_time: String?,
        val countryname: String?,
        val cover_photo: String?,
        val currency_type: String?,
        val delivery_amount: String?,
        val delivery_close_before_hours: String?,
        val delivery_email: String?,
        val description: String?,
        val discounted_from_time: Any,
        val discounted_price: Any,
        val discounted_to_time: Any,
        val distance: String?,
        val email: String?,
        val expected_description: String?,
        val fav_count: Int,
        val food_category: String?,
        val id: String?,
        val isCurrentLike: Int,
        val isOnlyforDonation: Int,
        val is_active: Int,
        val is_home_delivery: Int,
        val is_open: Int,
        val is_sponsor: String?,
        val itemleft: Int,
        val latitude: Double = 0.0,
        val logo: String,
        val longitude: Double = 0.0,
        val mobile: String?,
        val name: String?,
        val newsletter: Any,
        val opening_time: String?,
        val portion: String?,
        val postalcode: String?,
        val price: Float,
        val product_types: List<ProductType>,
        val shop_open_time: String?,
        val sponsor_portion: Int,
        val sponsor_product_amount: String?,
        val state: String?,
        val storename: String?,
        val time_zone: String?,
        val website_url: String?
    ) : ClusterItem {
        data class ProductType(
            val id: String,
            val product_type: String
        )

        override fun getTitle(): String? {
            return name
        }

        override fun getPosition(): LatLng {
            return LatLng(latitude, longitude)
        }

        override fun getSnippet(): String {
            return ""
        }
    }
}