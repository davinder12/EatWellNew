package com.android.mealpass.data.api

import com.android.mealpass.data.models.ReceiptResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path


interface ReceiptApi {

    companion object {
        const val MODULE_PATH = "portal/api/"
    }


    //TODO  check "cost_price" field value coming from backend side
    @FormUrlEncoded
    @POST("save_receipt.php")
    fun saveReceiptMethod(
        @Field("user_id") userId: String?,
        @Field("restaurent_id") resId: String?,
        @Field("collection_time") collectionTime: String?,
        @Field("quantity") qty: Int?,
        @Field("reference_id") refId: String?,
        @Field("payment_info") paymentInfo: String?,
        @Field("paymentMethod") paymentMethod: String?,
        @Field("amount") amount: Float?,
        @Field("isFromCampaign") campaign: String?,
        @Field("donated_amount") donatedamount: Float?,
        @Field("delivery_amount") deliveryamount: Float?,
        @Field("delivery_address") deliveryAddress: String?,
        @Field("isdelivery") isHomeDelivery: String?,
        @Field("retail_price") retailPrice: String?,
        @Field("cost_price") costPrice: Float?,
    ): Single<Response<JsonObject>>

    @FormUrlEncoded
    @POST("get_all_receipts.php")
    fun getReceiptId(@Field("user_id") userId: String?): Single<Response<ReceiptResponse>>


    @FormUrlEncoded
    @POST("updateOrderStatus.php")
    fun updateOrderStatus(@Field("receipt_id") userId: Int?,
    ):Single<Response<JsonObject>>




}