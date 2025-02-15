package com.android.eatwell.view.dashboard.fragment.setting.viewmodel


import com.android.eatwell.data.api.enums.SettingEnum
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject

@HiltViewModel
class SettingFragmentViewModel @Inject constructor(private val userRepository: UserRepository,
                                                   private val preferenceService: PreferenceService) :
    BaseViewModel(){


    var isLocationNotification = mutableLiveData(!preferenceService.getBoolean(R.string.pkey_local_notification))


    var settingList  = mutableLiveData(
            listOf(SettingEnum.ADD_REFERRAL_CODE, SettingEnum.ADD_STAFF_CODE, SettingEnum.PROFILE, SettingEnum.CHANGE_PASSWORD,
                    SettingEnum.CONTACT_US_FEEDBACK, SettingEnum.NOTIFICATION, SettingEnum.BUILD_VERSION,SettingEnum.DELETE_ACCOUNT, SettingEnum.LOGOUT)
    )

    fun updateNotification(){
        isLocationNotification.value = preferenceService.getBoolean(R.string.pkey_local_notification)
        preferenceService.putBoolean(R.string.pkey_local_notification,!(isLocationNotification.value?:false))
    }



}