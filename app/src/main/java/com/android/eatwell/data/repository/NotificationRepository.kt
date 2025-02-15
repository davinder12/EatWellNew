package com.android.eatwell.data.repository

import com.android.eatwell.data.api.NotificationApi
import com.android.eatwell.data.models.NotificationResponse
import com.android.eatwell.data.models.ResponseValidator
import com.android.eatwell.data.network.*
import com.android.eatwell.data.service.AuthState
import com.android.eatwell.data.network.AppExecutors
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class NotificationRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val notificationApi: NotificationApi,
    private val authState :AuthState
) {

    fun getNotificationList(userId: String?,createdDate:String?, timeZone:String?): IResource<NotificationResponse> {
        return NetworkResource(appExecutors, object : IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<NotificationResponse, NotificationResponse> {
            override fun mapToLocal(response: NotificationResponse): NotificationResponse {
                return response
            }
            override fun createNetworkRequest(): Single<Response<NotificationResponse>> {
                return notificationApi.getNotificationApi(userId,createdDate,timeZone)
            }
            override fun getResponseStatus(response: Response<NotificationResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }
}
