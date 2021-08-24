package com.android.mealpass.view.dashboard.fragment.setting.viewmodel


import com.android.mealpass.data.api.enums.SettingEnum
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class SettingFragmentViewModel @Inject constructor(private val userRepository: UserRepository,
                                                   private val preferenceService: PreferenceService) :
    BaseViewModel(){


    var isLocationNotification = mutableLiveData(!preferenceService.getBoolean(R.string.pkey_local_notification))


    var settingList  = mutableLiveData(
        listOf(SettingEnum.ADD_REFERRAL_CODE, SettingEnum.PROFILE,SettingEnum.CHANGE_PASSWORD,
            SettingEnum.CONTACT_US_FEEDBACK, SettingEnum.NOTIFICATION, SettingEnum.LOGOUT)
    )


    fun updateNotification(){
        isLocationNotification.value = preferenceService.getBoolean(R.string.pkey_local_notification)
        preferenceService.putBoolean(R.string.pkey_local_notification,!(isLocationNotification.value?:false))
    }



}