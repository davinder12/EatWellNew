package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import com.android.eatwell.data.extension.map
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class DeliveryAddressViewModel @Inject constructor(private val preferenceService: PreferenceService) : BaseViewModel() {


    var deliveryAddressList = MutableLiveData<List<String>>().also {
        it.value = getListOfObject()
    }
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


    fun removeItemFromList(position:Int){
        deliveryAddressList.value?.let {
            (it as ArrayList<String>).removeAt(position)
            deliveryAddressList.value = it
            preferenceService.putString(R.string.pkey_deliveryAddressList, Gson().toJson(it))
        }
    }


    private fun saveList(){
        preferenceService.putString(R.string.pkey_deliveryAddressList, Gson().toJson(tempDeliveryAddressList))
    }



    private fun getListOfObject(): ArrayList<String> {
        var response = arrayListOf<String>()
        try {
            preferenceService.getString(R.string.pkey_deliveryAddressList)?.let {
                if(it.isNotEmpty()) {
                    val type = object : TypeToken<ArrayList<String>>() {}.type
                    response = Gson().fromJson(it, type)
                }
            }
        }catch (e:Exception){}
        return response
    }



    fun updateList() {
        val address = flatHouseNo.value + ", " + colonyStreet.value + ", " + city.value + ", " + state.value + ", " + country.value + ", " + postalZipCode.value
        tempDeliveryAddressList.add(address)
        deliveryAddressList.value = tempDeliveryAddressList
        saveList()
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