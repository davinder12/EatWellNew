package com.android.mealpass.data.api

import com.android.mealpass.data.models.CommonResponseModel
import com.android.mealpass.data.models.MerchantNotificationResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path


interface MerchantApi {

    companion object {
        const val MODULE_PATH = "portal/api/"
    }




    @FormUrlEncoded
    @POST("get_current_portion.php")
    fun getPortionListApi(@Field("user_id") userId: String?,
                          @Field("time_zone") timezone: String?) : Single<Response<MerchantNotificationResponse>>



    @FormUrlEncoded
    @POST("save_description.php")
    fun merchantDescriptionApi(@Field("user_id") userId: String?,
                               @Field("expected_description") portion: String?):Single<Response<JsonObject>>

    @FormUrlEncoded
    @POST("save_current_portion.php")
    fun savePortionMethod(@Field("user_id") userId: String?,
                          @Field("portion") portion: Int?,
                          @Field("time_zone") timezone: String?): Single<Response<CommonResponseModel>>


    @FormUrlEncoded
    @POST("portion_price_notification.php")
    fun portionPriceNotfication(@Field("user_id") userId: String?,
                                @Field("portion_price") portion_price: String?,
                                @Field("currency_type") currency_type: String?,
                                @Field("is_portion") is_portion: String?,
                                @Field("portion") portion: Int?):  Single<Response<CommonResponseModel>>

    @FormUrlEncoded
    @POST("save_portion_price.php")
    fun updatRetailAndCostPrice(@Field("user_id") userId: String?,
                                @Field("retail_price") retailPrice: String?,
                                @Field("cost_price") costPrice: String?,
                                @Field("currency_type") currency_type: String?,
                                @Field("time_zone") timezone: String?): Single<Response<CommonResponseModel>>


    @FormUrlEncoded
    @POST("change_resturant_status.php")
    fun changeResturant(@Field("user_id") userId: String?,
                        @Field("is_open") isOpen: Int?): Single<Response<CommonResponseModel>>


}