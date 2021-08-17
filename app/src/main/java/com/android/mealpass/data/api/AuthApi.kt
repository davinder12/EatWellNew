package com.android.mealpass.data.api

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    companion object {
        const val MODULE_PATH = "portal/api/"
    }

    @POST("user_login.php/")
    @FormUrlEncoded
    fun apiUserLogin(
        @Field("email") username: String?,
        @Field("password") password: String?,
        @Field("device_type") devicetype: String?,
        @Field("device_token") device_token: String?,
    ): Single<Response<JsonObject>>

    @FormUrlEncoded
    @POST("signup_user.php")
    fun signUpMethod(
        @Field("name") firstname: String?,
        @Field("email") lastname: String?,
        @Field("mobile") order_id: String?,
        @Field("password") password: String?,
        @Field("newsletter") newsletter: Boolean?,
        @Field("device_token") fromUser: String?,
        @Field("device_type") device_type: String?,
        @Field("app_version") versionName: String?
    ): Single<Response<JsonObject>>


    @FormUrlEncoded
    @POST("user_forget_password.php")
    fun forgotPwdMethod(@Field("email") email: String?): Single<Response<JsonObject>>


    @FormUrlEncoded
    @POST("social_login.php")
    fun facebookLogin(
        @Field("social_id") socialId: String?,
        @Field("social_type") socialType: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("mobile") mobileno: String?,
        @Field("created_date") date: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?,
        @Field("app_version") appVersion: String?
    ): Single<Response<JsonObject>>
}

