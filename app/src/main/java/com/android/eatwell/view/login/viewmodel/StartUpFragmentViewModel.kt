package com.android.eatwell.view.login.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.models.LoginResponse
import com.android.eatwell.data.models.SignUpRequestModel
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseBackStack.Companion.DEVICE_TYPE
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.login.fragment.StartUpFragment.Companion.EMAIL
import com.android.eatwell.view.login.fragment.StartUpFragment.Companion.FACEBOOK_ID
import com.android.eatwell.view.login.fragment.StartUpFragment.Companion.NAME
import com.android.eatwell.view.login.fragment.StartUpFragment.Companion.SOCIAL_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class StartUpFragmentViewModel @Inject constructor(private val preferenceService: PreferenceService, private val userRepository: UserRepository
) :
    BaseViewModel() {


    var name = ""
    var emailId = ""
    var facebookId = ""


    fun getResponseFromFacebook(jsonObject: JSONObject, callBack: (Boolean, Int) -> Unit) {
        if (jsonObject.toString().contains(EMAIL)) {
            emailId = jsonObject.getString(EMAIL)
        }
        if (jsonObject.toString().contains(NAME)) {
            name = jsonObject.getString(NAME)
        }
        facebookId = jsonObject.getString(FACEBOOK_ID)
        when {
            facebookId.isEmpty() -> callBack(false, R.string.FailureMessage)
            else -> callBack(true, R.string.login_success)
        }
    }


    fun socialLoginMethod(): LiveData<NetworkState> {
        return userRepository.socialSignUp(
                SignUpRequestModel(BuildConfig.VERSION_NAME, preferenceService.getString(R.string.pkey_fcm_token_sent),
                        DEVICE_TYPE, emailId, "", name, false, "", facebookId,SOCIAL_TYPE,"")
        ).also {
            subscribe(it.request) { response -> response.body()?.let { updateUserData(it) } }
        }.networkState
    }



    private fun updateUserData(loginResponse: LoginResponse) {


        preferenceService.putString(R.string.pkey_user_Id, loginResponse.body.id)
        preferenceService.putBoolean(R.string.pkey_social_login,true)
        preferenceService.putString(R.string.pkey_secure_token, loginResponse.body.secure_key)
        preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)
        preferenceService.putString(R.string.pkey_social_emaiId, loginResponse.body.email)
    }
}

