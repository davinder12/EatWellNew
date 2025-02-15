package com.android.eatwell.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class FoodData(
    val body: List<Body>?,
    val status: Status
) {

    data class Status(
        val code: Int = 0,
        val message: String,
        val notification_Count: Int = 0,
        val total_count: String
    )

    data class Body(
        val address: String?,
        val allow_fullday_delivery: Boolean?,
        val allow_subscription: String?,
        val alternate_email: String?,
        val before_pickup_time: String?,
        val before_price: String?,
        val campaign_itemleft: Int? = 0,
        val city: String?,
        val closing_time: String?,
        val countryname: String?,
        val cover_photo: String?,
        val currency_type: String?,
        val delivery_amount: String?,
        val delivery_close_before_hours: String?,
        val delivery_email: String?,
        val description: String?,
        val distance: Float? = null,
        val email: String?,
        val expected_description: String?,
        val fav_count: Int? = 0,
        val food_category: String?,
        val id: String?,
        val isCurrentLike: Int? = 0,
        val isOnlyforDonation: Int? = 0,
        val is_active: Int? = 0,
        val is_home_delivery: Int? = 0,
        val is_open: Int? = 0,
        val is_sponsor: String?,
        val itemleft: Int? = 0,
        val latitude: Double? = 0.0,
        val logo: String?,
        val longitude: Double? = 0.0,
        val mobile: String?,
        val name: String?,
        val newsletter: Any?,
        val opening_time: String?,
        val portion: String?,
        val postalcode: String?,
        val price: Float?,
        val discounted_from_time: Any?,
        val discounted_price: Any?,
        val discounted_to_time: Any?,
        val product_types: List<ProductType>,
        val shop_open_time: String?,
        val sponsor_portion: Int? = 0,
        val sponsor_product_amount: String?,
        val state: String?,
        val storename: String?,
        val time_zone: String?,
        val website_url: String?,
        val is_only_for_staff: Boolean?,
        val is_staff_user: Boolean?
    ) : ClusterItem {
        data class ProductType(
            val id: String,
            val product_type: String
        )

        override fun getTitle(): String {
            return "$storename >"
        }

        override fun getPosition(): LatLng {
            return LatLng(latitude?:0.0, longitude?:0.0)
        }

        override fun getSnippet(): String {
            return ""
        }
    }
}
