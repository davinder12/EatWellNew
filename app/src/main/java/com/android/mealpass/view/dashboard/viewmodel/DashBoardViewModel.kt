package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.LocationRepository
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val preferenceService: PreferenceService,
        private val locationRepository: LocationRepository
) :
    BaseViewModel() {

    val userName = mutableLiveData("")
    val title = mutableLiveData("")

    init {
        userName.value = preferenceService.getString(R.string.pkey_userName)
    }

    fun clearData() {
        preferenceService.clearPreference()
    }

    fun isFirstTimeVisit() = preferenceService.getBoolean(R.string.pkey_first_video)

    fun getUserId(): String {
        return preferenceService.getString(R.string.pkey_user_Id, "") ?: ""
    }


    fun updateTitle(item: String) {
        title.value = item
    }

//
//    fun updateLocation() {
//        locationRepository.getUserCurrentLocation {
//            requestModel().also { subscribe(it.request) }
//        }
//    }
//
//
    fun logoutMethod(): LiveData<NetworkState> {
        return userRepository.logoutApi(preferenceService.getString(R.string.pkey_user_Id))
            .also { subscribe(it.request)
            }.networkState
    }


//    private fun requestModel(): IRequest<Response<LocationUpdateResponse>> {
//        return userRepository.updateLocation(
//            LocationUpdate(
//                BuildConfig.VERSION_NAME
//                , preferenceService.getString(R.string.pkey_location),
//                preferenceService.getString(R.string.pkey_fcm_token_sent),
//                preferenceService.getString(R.string.pkey_userlat),
//                preferenceService.getString(R.string.pkey_userLong),
//                preferenceService.getString(R.string.pkey_user_Id)
//            )
//        )
//    }
}