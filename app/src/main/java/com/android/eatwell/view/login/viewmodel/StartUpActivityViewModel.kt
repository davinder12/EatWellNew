package com.android.eatwell.view.login.viewmodel


import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject

@HiltViewModel
class StartUpActivityViewModel @Inject constructor(private val preferenceService: PreferenceService, private val userRepository: UserRepository
) :
    BaseViewModel() {

    fun updateDeviceToken(deviceToken: String) {
        preferenceService.putString(R.string.pkey_fcm_token_sent, deviceToken)
    }

}