package com.android.eatwell.data.api

import com.android.eatwell.data.models.NotificationResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*


interface NotificationApi {

    companion object {
        const val MODULE_PATH = "portal/api/"
    }


    @FormUrlEncoded
    @POST("getnotifications.php")
    fun getNotificationApi(@Field("user_id") userId: String?,
                           @Field("current_date") currentDate: String?,
                           @Field("time_zone") timezone: String?)
            : Single<Response<NotificationResponse>>


}