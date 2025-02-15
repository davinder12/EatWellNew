package com.android.eatwell.view.notification.viewmodel


import com.android.eatwell.data.extension.map
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.repository.NotificationRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.ResourceViewModel
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.units.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
        private val notificationRepository: NotificationRepository,
        private val preferenceService: PreferenceService
) : BaseViewModel() {


    var userId = mutableLiveData(preferenceService.getString(R.string.pkey_user_Id))

    var resource = ResourceViewModel(userId) {
        notificationRepository.getNotificationList(it, getCurrentDate(),TimeZone.getDefault().id)
    }

    var notificationList = resource.data.map {
        it.body
    }

    var networkState  =resource.networkState.map {
        it
    }

    fun updateBadgesCount() {
        var numberOfCount = preferenceService.getInt(R.string.pkey_notification_count)
        if (numberOfCount > 0) {
            numberOfCount -= 1
            preferenceService.putInt(R.string.pkey_notification_count, numberOfCount)
        }
    }

    fun callApi() {
        userId.value = preferenceService.getString(R.string.pkey_user_Id)
    }

    fun updateBadges() {
        preferenceService.putInt(R.string.pkey_notification_count, 0)
    }

}