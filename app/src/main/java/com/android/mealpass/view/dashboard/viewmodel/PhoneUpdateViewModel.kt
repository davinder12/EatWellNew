package com.android.mealpass.view.dashboard.viewmodel


import android.location.Location
import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.convertJsonToModelClass
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.PhoneUpdateResponse
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class PhoneUpdateViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {
    var category: String = ""
    var location: Location? = null


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
            response.body()?.let { jsonObject ->
                val convertedData = jsonObject.toString().convertJsonToModelClass {
                    Gson().fromJson(jsonObject.toString(), PhoneUpdateResponse::class.java)
                }
                convertedData?.let {
                    updatePhoneNumber(it)
                }
            }
        }
        return requestModel.networkState
    }

    private fun updatePhoneNumber(response: PhoneUpdateResponse) {
        preferenceService.putString(R.string.pkey_phoneNumber, response.body.mobile)


    }

}