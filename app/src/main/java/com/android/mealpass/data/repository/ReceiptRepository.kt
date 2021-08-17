package com.android.mealpass.data.repository

import com.android.mealpass.data.api.ReceiptApi
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.network.IRequest
import com.android.mealpass.data.network.IRetrofitNetworkRequestCallback
import com.android.mealpass.data.network.NetworkRequest
import com.exactsciences.portalapp.data.network.AppExecutors
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class ReceiptRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val receiptApi: ReceiptApi
) {


    fun campaignReceiptMethod(saveReceiptRequestModel: SaveReceiptRequestModel?): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return receiptApi.saveReceiptMethod(
                    saveReceiptRequestModel?.user_id,
                    saveReceiptRequestModel?.restaurent_id,
                    saveReceiptRequestModel?.collection_time,
                    saveReceiptRequestModel?.receipt_product_info?.get(0)?.quantity,
                    saveReceiptRequestModel?.reference_id,
                    saveReceiptRequestModel?.payment_info,
                    saveReceiptRequestModel?.payment_info,
                    saveReceiptRequestModel?.amount,
                    saveReceiptRequestModel?.isFromCampaign,
                    saveReceiptRequestModel?.donated_amount,
                    saveReceiptRequestModel?.delivery_amount,
                    saveReceiptRequestModel?.delivery_address,
                    saveReceiptRequestModel?.isHomeDelivery,
                    saveReceiptRequestModel?.receipt_product_info?.get(0)?.before_price,
                    saveReceiptRequestModel?.amount
                )
            }
        })
    }


}
