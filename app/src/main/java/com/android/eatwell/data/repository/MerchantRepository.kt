package com.android.eatwell.data.repository

import com.android.eatwell.data.api.MerchantApi
import com.android.eatwell.data.models.CommonResponseModel
import com.android.eatwell.data.models.MerchantNotificationResponse
import com.android.eatwell.data.models.ResponseValidator
import com.android.eatwell.data.network.*
import com.android.eatwell.data.service.AuthState
import com.android.eatwell.data.network.AppExecutors
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class MerchantRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val merchantApi: MerchantApi,
    private val authState :AuthState
) {

    fun getPortionListMethod(userId: String?,timeZone:String?): IResource<MerchantNotificationResponse> {
        return NetworkResource(appExecutors, object : IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<MerchantNotificationResponse, MerchantNotificationResponse> {
            override fun mapToLocal(response: MerchantNotificationResponse): MerchantNotificationResponse {
                return response
            }
            override fun createNetworkRequest(): Single<Response<MerchantNotificationResponse>> {
                return merchantApi.getPortionListApi(userId,timeZone)
            }
            override fun getResponseStatus(response: Response<MerchantNotificationResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun updateMerchantDescriptionMethod(userId: String?, description: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.merchantPushNotification(userId, description)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun updatePortionMethod(userId: String?, portion: Int?, timeZone: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.savePortionMethod(userId, portion, timeZone)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun updateProductDetailMethod(userId: String?, portion: Int?, retailPrice: String?, costPrice: String?, currencyType: String?, timeZone: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.updateProductDetailMethod(userId, portion,
                        retailPrice, costPrice, currencyType, timeZone)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun portionPriceNotificationMethod(userId: String?, portionPrice: String?, currencyType: String?, IsPortion: String?, portion: Int?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.portionPriceNotfication(userId, portionPrice, currencyType, IsPortion, portion)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }

            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun updateRetailAndCostPriceMethod(userId: String?, retailPrice: String?,costPrice: String?,currencyType:String?,timeZone:String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.updatRetailAndCostPrice(userId,retailPrice,costPrice,currencyType,timeZone )
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun changeResturantStatusMethod(userId: String?, isOpen: Int?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.changeResturant(userId,isOpen)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun updateMerchantToken(userId: String?, appVersion: String?,deviceType:String?,deviceToken:String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return merchantApi.merchantTokenUpdate(userId,appVersion,deviceType,deviceToken)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }









}
