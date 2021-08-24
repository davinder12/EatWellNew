package com.android.mealpass.data.api

import com.android.mealpass.data.models.ProfileResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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


    @POST("merchant_login.php")
    @FormUrlEncoded
    fun apiMerchantLogin(
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

    @FormUrlEncoded
    @POST("user_logout.php")
    fun logoutMethod(@Field("user_id") userId: String?): Single<Response<Unit>>

    @FormUrlEncoded
    @POST("signup_user_part2.php")
    fun referralCodeApi(
            @Field("user_id") userId: String?,
            @Field("email") email: String?,
            @Field("referral_code") referral_code: String?
    ): Single<Response<JsonObject>>


    @FormUrlEncoded
    @POST("user_update_pass.php")
    fun updatePassword(
            @Field("user_id") userId: String?,
            @Field("old_pass") oldPwd: String?,
            @Field("pass") newPwd: String?
    ): Single<Response<JsonObject>>


    @Multipart
    @POST("user_update_profile.php")
    fun updateProfile(@PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody?>): Single<Response<JsonObject>>

    @FormUrlEncoded
    @POST(" user_view_profile.php")
    fun viewProfile(@Field("user_id") userId: String?):Single<Response<ProfileResponse>>

    @FormUrlEncoded
    @POST("/{feedback}")
    fun feedBackApi(@Field("user_id") userId: String?,
                 @Field("title") limit: String?,
                 @Field("message") offset: String?): Single<Response<JsonObject>>

}

