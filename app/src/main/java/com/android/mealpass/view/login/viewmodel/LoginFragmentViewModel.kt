package com.android.mealpass.view.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.convertJsonToModelClass
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.LoginResponse
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject


@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {

    var emailId = mutableLiveData("shipra@ywasteapp.com")
    var password = mutableLiveData("123456@")


    fun callSignInApi(): LiveData<NetworkState> {
        return userRepository.signInApi(emailId.value, password.value, "0", "").also {
            subscribe(it.request) { response ->
                response.body()?.let { jsonObject ->
                    val convertedData = jsonObject.toString().convertJsonToModelClass {
                        Gson().fromJson(jsonObject.toString(), LoginResponse::class.java)
                    }
                    convertedData?.let {
                        updateUserData(it, false)
                    }
                }
            }
        }.networkState
    }


    fun merchantSignInMethod(): LiveData<NetworkState> {
        return userRepository.merchantLoginMethod(emailId.value, password.value, "0", "ssfss").also {
            subscribe(it.request) { response ->
                response.body()?.let { jsonObject ->
                    val convertedData = jsonObject.toString().convertJsonToModelClass {
                        Gson().fromJson(jsonObject.toString(), LoginResponse::class.java)
                    }
                    convertedData?.let {
                        updateUserData(it, true)
                    }
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


    private fun updateUserData(loginResponse: LoginResponse, isUserLogin: Boolean) {
        preferenceService.putBoolean(R.string.pkey_isMerchantLogin, isUserLogin)
        preferenceService.putString(R.string.pkey_user_Id, loginResponse.body.id)
        preferenceService.putBoolean(R.string.pkey_social_login,false)
        preferenceService.putString(R.string.pkey_secure_token, loginResponse.body.secure_key)
        preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)
        preferenceService.putString(R.string.pkey_emaiId, loginResponse.body.email)

//        preferenceService.putString(R.string.pkey_userName,loginResponse.body.name)
//        preferenceService.putString(R.string.pkey_emaiId,loginResponse.body.email)

    }


}
