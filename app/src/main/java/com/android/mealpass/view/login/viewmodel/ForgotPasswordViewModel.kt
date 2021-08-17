package com.android.mealpass.view.login.viewmodel


import android.util.Patterns
import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {


    var emailId = mutableLiveData("")

    fun callForgotPwdApi(): LiveData<NetworkState> {
        return userRepository.forgotPwdApi(emailId.value).also {
            subscribe(it.request)
        }.networkState
    }

    fun filterMethod(callBack: (Boolean, Int?) -> Unit) {
        when {
            emailId.value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailId.value ?: "")
                .matches() -> callBack(false, R.string.EnterEmail)
            else -> callBack(true, null)
        }
    }
}