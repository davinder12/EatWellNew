package com.android.mealpass.view.notification.viewmodel


import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.NotificationRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.units.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
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

    fun updateBadgesCount(){
        var numberOfCount = preferenceService.getInt(R.string.pkey_notification_count)
        if( numberOfCount > 0 ){
            numberOfCount -= 1
            preferenceService.putInt(R.string.pkey_notification_count,numberOfCount)
        }
    }

    fun updateBadges(){
        preferenceService.putInt(R.string.pkey_notification_count,0)
    }

}