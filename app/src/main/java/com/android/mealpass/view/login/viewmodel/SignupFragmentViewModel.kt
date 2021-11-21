package com.android.mealpass.view.login.viewmodel


import android.util.Patterns
import androidx.lifecycle.LiveData
import com.android.mealpass.data.api.enums.LoginCategory
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.LoginResponse
import com.android.mealpass.data.models.SignUpRequestModel
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseBackStack.Companion.DEVICE_TYPE
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.login.fragment.StartUpFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SignupFragmentViewModel @Inject constructor(private val userRepository: UserRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {


    var name = mutableLiveData("")
    var emailId = mutableLiveData("")
    var mobile = mutableLiveData("")
    var password = mutableLiveData("")
    var confirmPwd = mutableLiveData("")


    var socialUserName = ""
    var socialEmailId = ""
    var facebookId = ""


    fun callSignUpApi(newsLetter: Boolean): LiveData<NetworkState> {
        return userRepository.signUpMethod(
                SignUpRequestModel(
                        BuildConfig.VERSION_NAME, preferenceService.getString(R.string.pkey_fcm_token_sent),
                        DEVICE_TYPE, emailId.value, mobile.value, name.value, false, password.value)
        ).also {
            subscribe(it.request) { response -> response.body()?.let { updateUserData(it, LoginCategory.NORMAL_LOGIN) } }
        }.networkState
    }

    fun getResponseFromFacebook(jsonObject: JSONObject, callBack: (Boolean, Int) -> Unit) {
        if (jsonObject.toString().contains(StartUpFragment.EMAIL)) {
            socialEmailId = jsonObject.getString(StartUpFragment.EMAIL)
        }
        if (jsonObject.toString().contains(StartUpFragment.NAME)) {
            socialUserName = jsonObject.getString(StartUpFragment.NAME)
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
                        DEVICE_TYPE, socialEmailId, "", socialUserName, false, "", facebookId, StartUpFragment.SOCIAL_TYPE, "")
        ).also {
            subscribe(it.request) { response -> response.body()?.let { updateUserData(it, LoginCategory.SOCIAL_LOGIN) } }
        }.networkState
    }


    fun filterMethod(callBack: (Boolean, Int?) -> Unit) {
        when {
            name.value.isNullOrBlank() -> callBack(false, R.string.EnterName)
            emailId.value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailId.value ?: "")
                    .matches() -> callBack(false, R.string.EnterEmail)
            password.value.isNullOrBlank() || password.value?.length ?: 0 < 5 -> callBack(
                    false,
                    R.string.PasswordLimit
            )
            !password.value.equals(confirmPwd.value, ignoreCase = true) -> callBack(
                false,
                R.string.PasswordMismatch
            )
            else -> callBack(true, null)
        }
    }

    private fun updateUserData(loginResponse: LoginResponse, loginCategory: LoginCategory) {
        preferenceService.putBoolean(R.string.pkey_isMerchantLogin, false)
        preferenceService.putString(R.string.pkey_secure_token, loginResponse.body.secure_key)
        preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)
        preferenceService.putString(R.string.pkey_user_Id, loginResponse.body.id)

        when (loginCategory) {
            LoginCategory.SOCIAL_LOGIN -> {
                preferenceService.putBoolean(R.string.pkey_social_login, true)
                preferenceService.putString(R.string.pkey_social_emaiId, loginResponse.body.email)
            }
            else -> {
                preferenceService.putBoolean(R.string.pkey_social_login, false)
                preferenceService.putString(R.string.pkey_emaiId, loginResponse.body.email)
            }
        }


    }
}