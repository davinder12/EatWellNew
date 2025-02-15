package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.models.PhoneUpdateResponse
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.ProductRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject

@HiltViewModel
class PhoneUpdateViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {


    var phoneNumber = mutableLiveData("")

    fun filterMethod(callBack: (Boolean, Int?) -> Unit) {
        when {
            phoneNumber.value.isNullOrBlank() || phoneNumber.value?.length ?: 0 < 9 -> callBack(
                false,
                null
            )
            else -> callBack(true, null)
        }
    }

    fun updatePhoneNumber(): LiveData<NetworkState> {
        val userId = preferenceService.getString(R.string.pkey_user_Id, "") ?: ""
        val phoneNumber = phoneNumber.value ?: ""
        val requestModel = productRepository.updateProfileData(userId, phoneNumber)
        subscribe(requestModel.request) { response ->
            response.body()?.let { it ->
                updatePhoneNumber(it)
            }
        }
        return requestModel.networkState
    }

    private fun updatePhoneNumber(response: PhoneUpdateResponse) {
        preferenceService.putString(R.string.pkey_phoneNumber, response.body.mobile)


    }

}