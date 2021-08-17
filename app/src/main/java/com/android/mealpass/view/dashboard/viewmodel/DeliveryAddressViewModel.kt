package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryAddressViewModel @Inject constructor() : BaseViewModel() {


    var deliveryAddressList = MutableLiveData<List<String>>()
    var tempDeliveryAddressList = arrayListOf<String>()

    var flatHouseNo = MutableLiveData<String>()
    var colonyStreet = MutableLiveData<String>()
    var city = MutableLiveData<String>()
    var state = MutableLiveData<String>()
    var postalZipCode = MutableLiveData<String>()
    var country = MutableLiveData<String>()


    var isManualAddressFieldOpen = mutableLiveData(false)

    var isTextEditable = mutableLiveData("")

    var isTextEmptyOrNot = isTextEditable.map {
        !it.isNullOrBlank()
    }

    fun filterMethod(callBack: (Boolean) -> Unit) {
        when {
            flatHouseNo.value.isNullOrBlank() -> callBack(false)
            colonyStreet.value.isNullOrBlank() -> callBack(false)
            city.value.isNullOrBlank() -> callBack(false)
            state.value.isNullOrBlank() -> callBack(false)
            postalZipCode.value.isNullOrBlank() -> callBack(false)
            country.value.isNullOrBlank() -> callBack(false)
            else -> {
                callBack(true)
            }
        }
    }


    fun updateList() {
        val address =
            flatHouseNo.value + ", " + colonyStreet.value + ", " + city.value + ", " + state.value + ", " + country.value + ", " + postalZipCode.value
        tempDeliveryAddressList.add(address)
        deliveryAddressList.value = tempDeliveryAddressList
        clearData()
        isManualAddressFieldOpen.value = false
    }

    private fun clearData() {
        flatHouseNo.value = ""
        colonyStreet.value = ""
        city.value = ""
        state.value = ""
        postalZipCode.value = ""
        country.value = ""
    }
}