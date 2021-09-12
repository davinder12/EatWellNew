package com.android.mealpass.view.dashboard.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.LocationRepository
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
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



    var isLocalNotificationInit = mutableLiveData(preferenceService.getBoolean(R.string.pkey_local_notification_init))
    var updatedVersion =""
    var needToUpdateVersion = mutableLiveData(false)

    init {
        checkVersionOnPlayStore()
    }

    // TODO Foodfiltered value is not clear
    fun clearData() {
        val merchantRemeber =  preferenceService.getBoolean(R.string.pkey_isMerchantRemeber)
        val merchantEmailId = preferenceService.getString(R.string.pkey_merchantEmailId)
        val userEmailId = preferenceService.getString(R.string.pkey_emaiId)
        val customerRemeber = preferenceService.getBoolean(R.string.pkey_isCustomerRemeber)
        preferenceService.clearPreference()
        preferenceService.putBoolean(R.string.pkey_isMerchantRemeber, merchantRemeber)
        preferenceService.putString(R.string.pkey_merchantEmailId, merchantEmailId)
        preferenceService.putString(R.string.pkey_emaiId, userEmailId)
        preferenceService.putBoolean(R.string.pkey_isCustomerRemeber, customerRemeber)

    }


    private fun checkVersionOnPlayStore(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessTokenDeferred = async { playStoreApi() }
            compareVersion(accessTokenDeferred.await())
        }
    }

   private fun compareVersion(playStoreVersion: Float?) {
        CoroutineScope(Dispatchers.Main).launch {
            BuildConfig.VERSION_NAME.toFloatOrNull()?.let { currentVersion ->
                playStoreVersion?.let { playStoreVersion ->
                    if(currentVersion < playStoreVersion){
                        updatedVersion = playStoreVersion.toString()
                        needToUpdateVersion.postValue(true)
                    }
                }
            }
        }
    }

    private fun playStoreApi(): Float? {
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
        return newVersion.toFloatOrNull()
    }


    fun updateAlarmManagerStatus(){
        preferenceService.putBoolean(R.string.pkey_local_notification_init, true)
    }

    fun logoutMethod(): LiveData<NetworkState> {
        return userRepository.logoutApi(preferenceService.getString(R.string.pkey_user_Id))
            .also { subscribe(it.request)
            }.networkState
    }


}