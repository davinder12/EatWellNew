package com.android.eatwell.view.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import com.android.eatwell.data.api.enums.LoginCategory
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.models.LoginResponse
import com.android.eatwell.data.models.SignUpRequestModel
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseBackStack
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.login.fragment.StartUpFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {

    var emailId = mutableLiveData("")
    var password = mutableLiveData("")
    var isRememberMeChecked = mutableLiveData(false)
    var needToShowSocialLogin = mutableLiveData(false)


    var name = ""
    var socialEmailId = ""
    var facebookId = ""


    fun getResponseFromFacebook(jsonObject: JSONObject, callBack: (Boolean, Int) -> Unit) {
        if (jsonObject.toString().contains(StartUpFragment.EMAIL)) {
            socialEmailId = jsonObject.getString(StartUpFragment.EMAIL)
        }
        if (jsonObject.toString().contains(StartUpFragment.NAME)) {
            name = jsonObject.getString(StartUpFragment.NAME)
        }
        facebookId = jsonObject.getString(StartUpFragment.FACEBOOK_ID)
        when {
            facebookId.isEmpty() -> callBack(false, R.string.FailureMessage)
            else -> callBack(true, R.string.login_success)
        }
    }


    fun socialLoginMethod(): LiveData<NetworkState> {
        return userRepository.socialSignUp(
                SignUpRequestModel(BuildConfig.VERSION_NAME, preferenceService.getString(R.string.pkey_fcm_token_sent),
                        BaseBackStack.DEVICE_TYPE, socialEmailId, "", name, false, "", facebookId, StartUpFragment.SOCIAL_TYPE, "")
        ).also {
            subscribe(it.request) { response -> response.body()?.let { updateUserData(it, LoginCategory.SOCIAL_LOGIN) } }
        }.networkState
    }


    fun callSignInApi(): LiveData<NetworkState> {
        return userRepository.signInApi(emailId.value, password.value, "0", preferenceService.getString(R.string.pkey_fcm_token_sent)).also {
            subscribe(it.request) { response ->
                response.body()?.let {
                    updateUserData(it, LoginCategory.NORMAL_LOGIN)
                }
            }
        }.networkState
    }


    fun merchantSignInMethod(): LiveData<NetworkState> {
        return userRepository.merchantLoginMethod(emailId.value, password.value, "0", preferenceService.getString(R.string.pkey_fcm_token_sent,"no token available")).also {
            subscribe(it.request) { response ->
                response.body()?.let {
                    updateUserData(it, LoginCategory.MERCHANT_LOGIN)
                }
            }
        }.networkState
    }


    fun filterMethod(callBack: (Boolean, Int?) -> Unit) {
        when {
            emailId.value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailId.value ?: "")
                .matches() -> callBack(
                false,
                R.string.EnterEmail
            )
            password.value.isNullOrBlank() || password.value?.length ?: 0 < 5 -> callBack(
                false,
                R.string.PasswordLimit
            )
            else -> callBack(true, null)
        }
    }


    fun updateLoginType(isMerchantLoginOrNot: Boolean) {
        needToShowSocialLogin.value = isMerchantLoginOrNot
        if (isMerchantLoginOrNot) {
            isRememberMeChecked.value = preferenceService.getBoolean(R.string.pkey_isMerchantRemeber)
            if (isRememberMeChecked.value == true) emailId.value = preferenceService.getString(R.string.pkey_merchantEmailId)
        } else {
            isRememberMeChecked.value = preferenceService.getBoolean(R.string.pkey_isCustomerRemeber)
            if (isRememberMeChecked.value == true) emailId.value = preferenceService.getString(R.string.pkey_emaiId)
        }
    }


    private fun updateUserData(loginResponse: LoginResponse, loginCategory: LoginCategory) {
        when (loginCategory) {
            LoginCategory.MERCHANT_LOGIN ->
            {
                preferenceService.putString(R.string.pkey_merchantEmailId, loginResponse.body.email)
                preferenceService.putBoolean(R.string.pkey_isMerchantRemeber, isRememberMeChecked.value ?: false)
                preferenceService.putBoolean(R.string.pkey_social_login, false)
                preferenceService.putBoolean(R.string.pkey_isMerchantLogin, true)
            }
            LoginCategory.SOCIAL_LOGIN ->
            {
                preferenceService.putBoolean(R.string.pkey_social_login, true)
                preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)
                preferenceService.putString(R.string.pkey_social_emaiId, loginResponse.body.email)
            }
            else ->
            {
                preferenceService.putString(R.string.pkey_emaiId, loginResponse.body.email)
                preferenceService.putBoolean(R.string.pkey_isCustomerRemeber, isRememberMeChecked.value ?: false)
                preferenceService.putBoolean(R.string.pkey_social_login, false)
                preferenceService.putBoolean(R.string.pkey_isMerchantLogin, false)
            }
        }
        preferenceService.putString(R.string.pkey_user_Id, loginResponse.body.id)
        preferenceService.putString(R.string.pkey_secure_token, loginResponse.body.secure_key)
        preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)

    }



}
