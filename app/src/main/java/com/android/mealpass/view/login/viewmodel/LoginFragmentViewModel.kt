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

    var emailId = mutableLiveData("")
    var password = mutableLiveData("")
    var isRememberMeChecked = mutableLiveData(false)


    fun callSignInApi(): LiveData<NetworkState> {
        return userRepository.signInApi(emailId.value, password.value, "0", preferenceService.getString(R.string.pkey_fcm_token_sent)).also {
            subscribe(it.request) { response ->
                response.body()?.let {
                    updateUserData(it, false)
                }
            }
        }.networkState
    }


    fun merchantSignInMethod(): LiveData<NetworkState> {
        return userRepository.merchantLoginMethod(emailId.value, password.value, "0", preferenceService.getString(R.string.pkey_fcm_token_sent,"no token available")).also {
            subscribe(it.request) { response ->
                response.body()?.let {
                    updateUserData(it, true)
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


    fun updateLoginType(isMerchantLoginOrNot:Boolean){
        if(isMerchantLoginOrNot){
            isRememberMeChecked.value =  preferenceService.getBoolean(R.string.pkey_isMerchantRemeber)
            if(isRememberMeChecked.value == true) emailId.value =  preferenceService.getString(R.string.pkey_merchantEmailId)
        }else{
            isRememberMeChecked.value =  preferenceService.getBoolean(R.string.pkey_isCustomerRemeber)
            if(isRememberMeChecked.value == true) emailId.value = preferenceService.getString(R.string.pkey_emaiId)
        }
    }

    private fun updateUserData(loginResponse: LoginResponse, isMerchangLogin: Boolean) {
        when  {
            isMerchangLogin -> {
                preferenceService.putString(R.string.pkey_merchantEmailId, loginResponse.body.email)
                preferenceService.putBoolean(R.string.pkey_isMerchantRemeber, isRememberMeChecked.value?:false)
            }
            else ->{
                preferenceService.putString(R.string.pkey_emaiId, loginResponse.body.email)
                preferenceService.putBoolean(R.string.pkey_isCustomerRemeber, isRememberMeChecked.value?:false)
            }
        }
        preferenceService.putBoolean(R.string.pkey_isMerchantLogin, isMerchangLogin)
        preferenceService.putString(R.string.pkey_user_Id, loginResponse.body.id)
        preferenceService.putBoolean(R.string.pkey_social_login,false)
        preferenceService.putString(R.string.pkey_secure_token, loginResponse.body.secure_key)
        preferenceService.putString(R.string.pkey_phoneNumber, loginResponse.body.mobile)

    }



}
