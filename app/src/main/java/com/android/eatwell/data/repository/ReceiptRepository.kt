package com.android.eatwell.data.repository

import com.android.eatwell.data.api.ReceiptApi
import com.android.eatwell.data.models.*
import com.android.eatwell.data.network.*
import com.android.eatwell.data.service.AuthState
import com.android.eatwell.data.network.AppExecutors
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class ReceiptRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val receiptApi: ReceiptApi,
    private val authState: AuthState
) {


    fun campaignReceiptMethod(saveReceiptRequestModel: SaveReceiptRequestModel?): IRequest<Response<ProductReceiptResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<ProductReceiptResponse> {
            override fun createNetworkRequest(): Single<Response<ProductReceiptResponse>> {
                return receiptApi.saveReceiptMethod(
                        saveReceiptRequestModel?.user_id,
                        saveReceiptRequestModel?.restaurent_id,
                        saveReceiptRequestModel?.collection_time,
                        saveReceiptRequestModel?.total_quantity,
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
                        saveReceiptRequestModel?.receipt_product_info?.get(0)?.price, saveReceiptRequestModel?.isStaffReceipt
                )
            }

            override fun getResponseStatus(response: Response<ProductReceiptResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun getReceiptMethod(userId: String?): IResource<ReceiptResponse> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<ReceiptResponse, ReceiptResponse> {
            override fun mapToLocal(response: ReceiptResponse): ReceiptResponse {
                return response
            }
            override fun createNetworkRequest(): Single<Response<ReceiptResponse>> {
                return receiptApi.getReceiptId(userId)
            }

            override fun getResponseStatus(response: Response<ReceiptResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }

        })
    }


    fun redeemOrderMethod(receiptId: Int?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return receiptApi.updateOrderStatus(receiptId)
            }


            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun cancelOrderMethod(userId: String?, orderId: Int?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return receiptApi.cancelOrder(userId, orderId)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


}
