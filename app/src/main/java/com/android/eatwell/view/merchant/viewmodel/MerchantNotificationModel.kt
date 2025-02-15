package com.android.eatwell.view.merchant.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.eatwell.data.models.MerchantNotificationResponse
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.MerchantRepository
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.ResourceViewModel
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MerchantNotificationModel @Inject constructor(
        private val preferenceService: PreferenceService,
        private val  merchantRepository: MerchantRepository,
        private val  userRepository: UserRepository
) : BaseViewModel() {

    private val userId = MutableLiveData<String>()

    var price = 0.0
    var portion = 0
    var description = ""
    var statusCode = 0

    var merchantNotificationResponse : MerchantNotificationResponse.Body? = null


     val merchantResource = ResourceViewModel(userId) {
        merchantRepository.getPortionListMethod(it, TimeZone.getDefault().id)
    }


    val notificationList = merchantResource.data.map {
        merchantNotificationResponse = it.body
        description = it.body.expected_description ?: ""
        it.body.notifictions
    }

    val networkState = merchantResource.networkState.map {
        it
    }


    fun updateMerchantToken() {
        preferenceService.getString(R.string.pkey_fcm_token_sent)?.let {
            if(it.isNotEmpty()) {
                merchantRepository.updateMerchantToken(
                        preferenceService.getString(R.string.pkey_user_Id, ""), BuildConfig.VERSION_NAME, "0", preferenceService.getString(R.string.pkey_fcm_token_sent),
                ).also {
                    subscribe(it.request){
                       it.body()?.status?.code?.let {
                           statusCode = it
                       }
                    }
                }
            }
        }
    }

     fun callApi(){
        userId.value = preferenceService.getString(R.string.pkey_user_Id,"")
     }


    fun clearData() {
        val merchantRemeber =  preferenceService.getBoolean(R.string.pkey_isMerchantRemeber)
        val merchantEmailId = preferenceService.getString(R.string.pkey_merchantEmailId)
        val userEmailId = preferenceService.getString(R.string.pkey_emaiId)
        val customerRemeber = preferenceService.getBoolean(R.string.pkey_isCustomerRemeber)
        preferenceService.clearPreference()
        preferenceService.putBoolean(R.string.pkey_isMerchantRemeber,merchantRemeber)
        preferenceService.putString(R.string.pkey_merchantEmailId,merchantEmailId)
        preferenceService.putString(R.string.pkey_emaiId,userEmailId)
        preferenceService.putBoolean(R.string.pkey_isCustomerRemeber,customerRemeber)
    }

    fun logoutMethod(): LiveData<NetworkState> {
        return userRepository.logoutApi(preferenceService.getString(R.string.pkey_user_Id))
            .also { subscribe(it.request)
            }.networkState
    }




}