package com.android.mealpass.view.dashboard.viewmodel


import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.models.FoodRequestModel
import com.android.mealpass.data.repository.LocationRepository
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.PagedListViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.CATEGORY
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.CURRENT_LIST_SIZE
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.OFFSET
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.RESTURANT_CLOSE
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.RESTURANT_OPEN
import com.android.mealpass.view.units.getCurrentTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FindFoodViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService,
    private val locationRepository: LocationRepository
) :
    BaseViewModel() {
    var category: String = ""
    var location: Location? = null


    private val search = MutableLiveData<String>()


    var foodResource = PagedListViewModel(search) {
        productRepository.searchApi(
            FoodRequestModel(
                currentTimeValidation(),
                preferenceService.getString(R.string.pkey_userlat),
                CURRENT_LIST_SIZE,
                preferenceService.getString(R.string.pkey_userLong),
                OFFSET,
                TimeZone.getDefault().id,
                preferenceService.getString(R.string.pkey_user_Id),
                it,
                preferenceService.getString(R.string.pkey_location, ""),
                CATEGORY,
                preferenceService.getString(R.string.pkey_showOpenResturnat, RESTURANT_CLOSE),
                preferenceService.getString(R.string.pkey_toTime, ""),
                preferenceService.getString(R.string.pkey_fromTime, ""),
                getListOfObject()
            )
        )
    }

    private fun currentTimeValidation(): String {
        var resturantTime = ""
        preferenceService.getString(R.string.pkey_showOpenResturnat, RESTURANT_CLOSE)?.let {
            resturantTime = when (it) {
                RESTURANT_OPEN -> getCurrentTime()
                else -> ""
            }
        }
        return resturantTime
    }

    fun searchText(searchText: String) {
        when {
            preferenceService.getString(R.string.pkey_userlat)
                .isNullOrEmpty() -> locationRepository.getUserCurrentLocation {
                search.postValue(
                    searchText
                )
            }
            else -> {
                search.postValue(searchText)
                locationRepository.getUserCurrentLocation()
            }
        }
    }

    private fun getListOfObject(): List<String> {
        var response = listOf<String>()
        try {
            preferenceService.getString(R.string.pkey_filteredFoodList)?.let {
                val type = object : TypeToken<ArrayList<String>>() {}.type
                response = Gson().fromJson(it, type)

            }
        } catch (e: Exception) {
        }
        return response
    }


}