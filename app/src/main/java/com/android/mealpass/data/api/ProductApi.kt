package com.android.mealpass.data.api

import com.android.mealpass.data.models.FoodData
import com.android.mealpass.data.models.ProductTypeResponse
import com.android.mealpass.data.models.SpecificFoodResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ProductApi {

    companion object {
        const val MODULE_PATH = "portal/api/"
    }


    @FormUrlEncoded
    @POST("search.php")
    fun searchApiMethod(
        @Field("user_id") userId: String?,
        @Field("latitude") lat: String?,
        @Field("longitude") long1: String?,
        @Field("limit") limit: Int?,
        @Field("offset") offset: Int?,
        @Field("keyword") keyword: String?,
        @Field("country") country: String?,
        @Field("time_zone") timezone: String?,
        @Field("food_category") food_category: String?,
        @Field("filter_isopen") isopen: String?,
        @Field("filter_pickup_totime") toTime: String?,
        @Field("filter_pickup_fromtime") fromTime: String?,
        @Field("filter_product_type[]") list: List<String>?,
        @Field("current_time") currentTime: String?
    ): Call<FoodData>

    @FormUrlEncoded
    @POST("favourite_res.php")
    fun getFavouriteApi(
        @Field("user_id") userId: String?,
        @Field("latitude") lat: String?,
        @Field("longitude") long1: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int,
        @Field("time_zone") timezone: String?,
    ): Single<Response<FoodData>>


    @FormUrlEncoded
    @POST("get_all_res.php")
    fun getAllResturantList(
        @Field("user_id") userId: String?,
        @Field("time_zone") timezone: String?,
        @Field("food_category") food_category: String?,
        @Field("filter_isopen") isopen: String?,
        @Field("filter_pickup_totime") toTime: String?,
        @Field("filter_pickup_fromtime") fromTime: String?,
        @Field("filter_product_type[]") list: List<String>?,
        @Field("current_time") currentTime: String?
    ): Single<Response<FoodData>>


    @FormUrlEncoded
    @POST("get_single_res.php")
    fun getSingleRes(
        @Field("user_id") userId: String?,
        @Field("res_id") resID: String?,
        @Field("latitude") lat: String?,
        @Field("longitude") long1: String?,
        @Field("notify_id") singleRes: String?,
        @Field("time_zone") timezone: String?,
    ): Single<Response<SpecificFoodResponse>>


    @FormUrlEncoded
    @POST("add_favourite.php")
    fun likeResturantApi(
        @Field("favourite_id") favouriteId: String?,
        @Field("user_id") userId: String?,
    ): Single<Response<Unit>>

    @FormUrlEncoded
    @POST("delete_favourite.php")
    fun deleteResturantApi(
        @Field("favourite_id") favouriteId: String?,
        @Field("user_id") userId: String?,
    ): Single<Response<Unit>>



    @FormUrlEncoded
    @POST("get_product_types.php")
    fun getFoodType(
        @Field("user_id") userId: String?
    ): Single<Response<ProductTypeResponse>>


    @Multipart
    @POST("user_update_profile.php")
    fun updateProfile(@PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): Single<Response<JsonObject>>


}