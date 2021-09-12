package com.android.mealpass.view.login.viewmodel


import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class StartUpActivityViewModel @Inject constructor(private val preferenceService: PreferenceService, private val userRepository: UserRepository
) :
    BaseViewModel() {

    fun updateDeviceToken(deviceToken: String) {
        preferenceService.putString(R.string.pkey_fcm_token_sent, deviceToken)
    }

}