package com.android.mealpass.data.repository

import com.android.mealpass.data.api.AuthApi
import com.android.mealpass.data.models.*
import com.android.mealpass.data.network.*
import com.android.mealpass.data.service.AuthState
import com.exactsciences.portalapp.data.network.AppExecutors
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val authApi: AuthApi,
    private val authState: AuthState

) {


    companion object {

        const val SESSION_EXPIRED = 2
        const val RESPONSE_FALIURE = 0
        const val PLAIN_TEXT = "text/plain"
        const val FILE_FORMAT ="image/*"
    }


    fun signInApi(
        email: String?,
        password: String?,
        deviceType: String?,
        deviceToken: String?
    ): IRequest<Response<LoginResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<LoginResponse> {
            override fun createNetworkRequest(): Single<Response<LoginResponse>> {
                return authApi.apiUserLogin(email, password, deviceType, deviceToken)
            }
            override fun getResponseStatus(response: Response<LoginResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
        })
    }

    fun termConditionMethod(isMerchantLogin:String?): IRequest<Response<TermConditionResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<TermConditionResponse> {
            override fun createNetworkRequest(): Single<Response<TermConditionResponse>> {
                return authApi.getTermCondition(isMerchantLogin)
            }

            override fun getResponseStatus(response: Response<TermConditionResponse>): ResponseValidator {
               return ResponseValidator(1,"")
            }
        })
    }


    fun merchantLoginMethod(
            email: String?,
            password: String?,
            deviceType: String?,
            deviceToken: String?
    ): IRequest<Response<LoginResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<LoginResponse> {
            override fun createNetworkRequest(): Single<Response<LoginResponse>> {
                return authApi.apiMerchantLogin(email, password, deviceType, deviceToken)
            }
            override fun getResponseStatus(response: Response<LoginResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
        })
    }


    fun signUpMethod(signUpRequestModel: SignUpRequestModel): IRequest<Response<LoginResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<LoginResponse> {
            override fun createNetworkRequest(): Single<Response<LoginResponse>> {
                return authApi.signUpMethod(
                    signUpRequestModel.name, signUpRequestModel.email,
                    signUpRequestModel.mobile, signUpRequestModel.password,
                    signUpRequestModel.newsletter, signUpRequestModel.device_token,
                    signUpRequestModel.device_type, signUpRequestModel.version_name
                )
            }
            override fun getResponseStatus(response: Response<LoginResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
        })
    }

    fun forgotPwdApi(emailId: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return authApi.forgotPwdMethod(emailId)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
        })
    }


    fun socialSignUp(signUpRequestModel: SignUpRequestModel): IRequest<Response<LoginResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<LoginResponse> {
            override fun createNetworkRequest(): Single<Response<LoginResponse>> {
                return authApi.socialLogin(
                    signUpRequestModel.name, signUpRequestModel.email,
                    signUpRequestModel.mobile, signUpRequestModel.password,
                    signUpRequestModel.newsletter, signUpRequestModel.device_token,
                    signUpRequestModel.device_type, signUpRequestModel.version_name, signUpRequestModel.socialId, signUpRequestModel.socialType, signUpRequestModel.createdDate
                )
            }
            override fun getResponseStatus(response: Response<LoginResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
        })
    }


    fun logoutApi(userId: String?): IRequest<Response<Unit>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<Unit> {
            override fun createNetworkRequest(): Single<Response<Unit>> {
                return authApi.logoutMethod(userId)
            }

            override fun getResponseStatus(response: Response<Unit>): ResponseValidator {
               return ResponseValidator(1,"")
            }
        })
    }

    fun referralCodeMethod(userId: String?,emailId:String?,referalCode:String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return authApi.referralCodeApi(userId,emailId,referalCode)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun updatePasswordMethod(userId: String?,oldPassword:String?,newPassword:String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return authApi.updatePassword(userId,oldPassword,newPassword)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun updateProfileData(map: HashMap<String, RequestBody?>, imageUrl: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return if(imageUrl.isNullOrEmpty() || imageUrl.contains("http")) authApi.updateProfileData(map) else
                    authApi.updateProfile(map,File(imageUrl).multiPartObject("user_image"))
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun getProfileData(userId: String?): IResource<ProfileResponse> {
        return NetworkResource(
            appExecutors,
            object : IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<ProfileResponse, ProfileResponse> {
                override fun mapToLocal(response: ProfileResponse): ProfileResponse {
                    return response
                }
                override fun createNetworkRequest(): Single<Response<ProfileResponse>> {
                    return authApi.viewProfile(userId)
                }
                override fun getResponseStatus(response: Response<ProfileResponse>): ResponseValidator {
                    return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
                }
                override fun sessionExpired() {
                    authState.logout()
                }
            }
        )
    }


    fun feedBackMethod(userId: String?,title:String?,message:String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return authApi.feedBackApi(userId,title,message)
            }

            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun File.multiPartObject(key:String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, this.name, this.asRequestBody(FILE_FORMAT.toMediaTypeOrNull()))
    }




}
