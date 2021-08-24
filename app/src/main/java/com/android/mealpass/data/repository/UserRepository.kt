package com.android.mealpass.data.repository

import com.android.mealpass.data.api.AuthApi
import com.android.mealpass.data.models.ProfileResponse
import com.android.mealpass.data.models.SignUpRequestModel
import com.android.mealpass.data.network.*
import com.exactsciences.portalapp.data.network.AppExecutors
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val authApi: AuthApi
) {


    companion object {
        const val RESPONSE_SUCCESS = 1
        const val PLAIN_TEXT = "text/plain"
        const val FILE_FORMAT ="image/*"
    }


    fun signInApi(
        email: String?,
        password: String?,
        deviceType: String?,
        deviceToken: String?
    ): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.apiUserLogin(email, password, deviceType, deviceToken)
            }

            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }


    fun merchantLoginMethod(
            email: String?,
            password: String?,
            deviceType: String?,
            deviceToken: String?
    ): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.apiMerchantLogin(email, password, deviceType, deviceToken)
            }

            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }


    fun signUpMethod(signUpRequestModel: SignUpRequestModel): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.signUpMethod(
                    signUpRequestModel.name, signUpRequestModel.email,
                    signUpRequestModel.mobile, signUpRequestModel.password,
                    signUpRequestModel.newsletter, signUpRequestModel.device_token,
                    signUpRequestModel.device_type, signUpRequestModel.version_name
                )
            }

            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }

    fun forgotPwdApi(emailId: String?): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.forgotPwdMethod(emailId)
            }

            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }

    fun socialSignUp(signUpRequestModel: SignUpRequestModel): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.signUpMethod(
                    signUpRequestModel.name, signUpRequestModel.email,
                    signUpRequestModel.mobile, signUpRequestModel.password,
                    signUpRequestModel.newsletter, signUpRequestModel.device_token,
                    signUpRequestModel.device_type, signUpRequestModel.version_name
                )
            }
        })
    }


    fun logoutApi(userId: String?): IRequest<Response<Unit>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<Unit> {
            override fun createNetworkRequest(): Single<Response<Unit>> {
                return authApi.logoutMethod(userId)
            }
        })
    }

    fun referralCodeMethod(userId: String?,emailId:String?,referalCode:String?): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.referralCodeApi(userId,emailId,referalCode)
            }
            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }

    fun updatePasswordMethod(userId: String?,oldPassword:String?,newPassword:String?): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.updatePassword(userId,oldPassword,newPassword)
            }
            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }

    fun updateProfileData(
        map: HashMap<String, RequestBody?>): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.updateProfile(map)
            }
            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }


    fun getProfileData(userId: String?): IResource<ProfileResponse> {
        return NetworkResource(
            appExecutors,
            object :
                IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<ProfileResponse, ProfileResponse> {
                override fun mapToLocal(response: ProfileResponse): ProfileResponse {
                    return response
                }
                override fun createNetworkRequest(): Single<Response<ProfileResponse>> {
                    return authApi.viewProfile(userId)
                }
            }
        )
    }


    fun feedBackMethod(userId: String?,title:String?,message:String?): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                return authApi.feedBackApi(userId,title,message)
            }
            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }




}
