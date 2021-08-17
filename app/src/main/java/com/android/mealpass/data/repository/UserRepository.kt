package com.android.mealpass.data.repository

import com.android.mealpass.data.api.AuthApi
import com.android.mealpass.data.models.SignUpRequestModel
import com.android.mealpass.data.network.IRequest
import com.android.mealpass.data.network.IRetrofitNetworkRequestCallback
import com.android.mealpass.data.network.NetworkRequest
import com.exactsciences.portalapp.data.network.AppExecutors
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val authApi: AuthApi
) {


    companion object {
        const val RESPONSE_SUCCESS = 1
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

}
