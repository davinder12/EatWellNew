package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.models.ProductTypeResponse
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.LocationRepository
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.dashboard.DashboardActivity.Companion.VERSION_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val preferenceService: PreferenceService,
        private val locationRepository: LocationRepository
) :
    BaseViewModel() {


    var foodList: List<ProductTypeResponse.Body>? = null

    var versionNetworkState = MutableLiveData<NetworkState>()
    var isLocalNotificationInit = mutableLiveData(preferenceService.getBoolean(R.string.pkey_local_notification_init))
    var updatedVersion = ""
    var needToUpdateVersion = true

    // TODO Foodfiltered value is not clear
    fun clearData() {
        val merchantRemeber = preferenceService.getBoolean(R.string.pkey_isMerchantRemeber)
        val merchantEmailId = preferenceService.getString(R.string.pkey_merchantEmailId)
        val userEmailId = preferenceService.getString(R.string.pkey_emaiId)
        val customerRemeber = preferenceService.getBoolean(R.string.pkey_isCustomerRemeber)
        preferenceService.clearPreference()
        preferenceService.putBoolean(R.string.pkey_isMerchantRemeber, merchantRemeber)
        preferenceService.putString(R.string.pkey_merchantEmailId, merchantEmailId)
        preferenceService.putString(R.string.pkey_emaiId, userEmailId)
        preferenceService.putBoolean(R.string.pkey_isCustomerRemeber, customerRemeber)

    }


    fun checkVersionOnPlayStore() {
        versionNetworkState.postValue(NetworkState.loading)
        CoroutineScope(Dispatchers.IO).launch {
            val accessTokenDeferred = async { playStoreApi() }
            compareVersion(accessTokenDeferred.await())
        }
    }

    private fun compareVersion(playStoreVersion: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            BuildConfig.VERSION_NAME.toFloatOrNull()?.let { currentVersion ->
                when {
                    playStoreVersion.isNullOrEmpty() -> versionNetworkState.postValue(NetworkState.error(VERSION_MESSAGE))
                    else -> {
                        playStoreVersion.toFloatOrNull()?.let { playStoreVersion ->
                            if (currentVersion < playStoreVersion) {
                                needToUpdateVersion = true
                                updatedVersion = playStoreVersion.toString()
                                versionNetworkState.postValue(NetworkState.success)
                            } else {
                                needToUpdateVersion = false
                                versionNetworkState.postValue(NetworkState.error(VERSION_MESSAGE))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playStoreApi(): String {
        var newVersion = ""
        try {
            val document: Document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()

            val element: Elements = document.getElementsContainingOwnText("Current Version")
            for (ele in element) {
                if (ele.siblingElements() != null) {
                    val sibElemets: Elements = ele.siblingElements()
                    for (sibElemet in sibElemets) {
                        newVersion = sibElemet.text()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return newVersion
    }


    fun updateAlarmManagerStatus(){
        preferenceService.putBoolean(R.string.pkey_local_notification_init, true)
    }

    fun logoutMethod(): LiveData<NetworkState> {
        return userRepository.logoutApi(preferenceService.getString(R.string.pkey_user_Id))
            .also { subscribe(it.request)
            }.networkState
    }

    fun deleteMethod(): LiveData<NetworkState>{
        return userRepository.deleteApi(preferenceService.getString(R.string.pkey_user_Id))
            .also { subscribe(it.request)
            }.networkState

    }


}