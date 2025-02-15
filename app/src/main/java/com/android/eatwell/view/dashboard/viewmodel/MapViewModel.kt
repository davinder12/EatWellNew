package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import com.android.eatwell.data.extension.map
import com.android.eatwell.data.models.GetAllResturantRequest
import com.android.eatwell.data.models.SearchApiResponse
import com.android.eatwell.data.repository.LocationRepository
import com.android.eatwell.data.repository.ProductRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.ResourceViewModel
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.dashboard.fragment.HomeFragment
import com.android.eatwell.view.dashboard.fragment.HomeFragment.Companion.CATEGORY
import com.android.eatwell.view.units.getCurrentTime
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService,
    private val locationRepository: LocationRepository
) :
    BaseViewModel() {


    var userId = MutableLiveData<String>()
    var latitude = 0.0
    var longitude = 0.0


    var productList = MutableLiveData<List<SearchApiResponse.Body.ProductInfo>>()


    private var resourceModel = ResourceViewModel(userId) {
        productRepository.getAllResturantList(
            GetAllResturantRequest(
                getListOfObject(),
                CATEGORY,
                preferenceService.getString(R.string.pkey_userlat,"0.0"),
                preferenceService.getString(R.string.pkey_userLong,"0.0"),
                TimeZone.getDefault().id, it,
                preferenceService.getString(R.string.pkey_showOpenResturnat, "0"),
                preferenceService.getString(R.string.pkey_toTime, ""),
                preferenceService.getString(R.string.pkey_fromTime, ""),
                currentTimeValidation()
            )
        )
    }

    private fun currentTimeValidation(): String {
        var resturantTime = ""
        preferenceService.getString(R.string.pkey_showOpenResturnat, HomeFragment.RESTURANT_CLOSE)
            ?.let {
                resturantTime = when (it) {
                    HomeFragment.RESTURANT_OPEN -> getCurrentTime()
                    else -> ""
                }
            }
        return resturantTime
    }


    var data = resourceModel.data.map {
        it.body?.filter { it.is_active == ProductRepository.RESTURANT_ACTIVE }
    }

    var networkState = resourceModel.networkState.map {
        it
    }

    fun getLocation(): LatLng {
        preferenceService.getString(R.string.pkey_userlat, "0.0")?.toDoubleOrNull()
            ?.let { latitude = it }
        preferenceService.getString(R.string.pkey_userLong, "0.0")?.toDoubleOrNull()
            ?.let { longitude = it }
        return LatLng(latitude, longitude)
    }

    fun getResturantList() {
        when {
            preferenceService.getString(R.string.pkey_userlat).isNullOrEmpty() -> locationRepository.getUserCurrentLocation {
                this.userId.postValue(preferenceService.getString(R.string.pkey_user_Id, ""))
            }
            else -> {
                this.userId.postValue(preferenceService.getString(R.string.pkey_user_Id, ""))
                locationRepository.getUserCurrentLocation()
            }
        }
    }


    fun updateProductList(it: ArrayList<SearchApiResponse.Body.ProductInfo>) {
        productList.value = it
    }


    private fun getListOfObject(): List<String> {
        var response = listOf<String>()
        try {
            preferenceService.getString(R.string.pkey_filteredFoodList)?.let {
                val type = object : TypeToken<java.util.ArrayList<String>>() {}.type
                response = Gson().fromJson(it, type)

            }
        } catch (e: Exception) {
        }
        return response
    }

}