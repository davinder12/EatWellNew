package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.FoodRequestModel
import com.android.mealpass.data.repository.LocationRepository
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.PagedListViewModel
import com.android.mealpass.utilitiesclasses.ResourceViewModel
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mealpass.com.mealpass.BuildConfig
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


    var isFirstTimeLoadData = true
    var isNeedToUpdateDataFirstTime = true
    var isNeedToUpdateAdsList = true
    var country = MutableLiveData<String>()
    var listSize = 0
    var updatedOffset = OFFSET
    var notificationCount = mutableLiveData(0)


  //  preferenceService.getString(R.string.pkey_location, "")

    private val search = MutableLiveData<String>()
    var foodResource = PagedListViewModel(search) {
        productRepository.searchApi(
                FoodRequestModel(
                        currentTimeValidation(),
                        preferenceService.getString(R.string.pkey_userlat), getCurrentListSize(),
                        preferenceService.getString(R.string.pkey_userLong), OFFSET,
                        TimeZone.getDefault().id,
                        preferenceService.getString(R.string.pkey_user_Id),
                        it, countryName = if (it.isNullOrEmpty()) preferenceService.getString(R.string.pkey_location) else "",
                        CATEGORY,
                        preferenceService.getString(R.string.pkey_showOpenResturnat, RESTURANT_CLOSE),
                        preferenceService.getString(R.string.pkey_toTime, ""),
                        preferenceService.getString(R.string.pkey_fromTime, ""),
                        getListOfObject(), CURRENT_LIST_SIZE, updatedOffset
                )) {
            updatedOffset = it
        }
    }

    fun resetItem() {
        listSize = CURRENT_LIST_SIZE
        updatedOffset = 0
    }

    private fun getCurrentListSize(): Int {
        return if (listSize > 0) listSize else CURRENT_LIST_SIZE
    }


    var foodApiNetworkState = foodResource.networkState.map {
        it
    }

     fun locationUpdateApi(){
            if(!preferenceService.getString(R.string.pkey_userlat).isNullOrEmpty() && isNeedToUpdateDataFirstTime ){
                productRepository.locationUpdateMethod(
                    preferenceService.getString(R.string.pkey_user_Id),
                    preferenceService.getString(R.string.pkey_userlat),
                    preferenceService.getString(R.string.pkey_userLong),
                    BuildConfig.VERSION_NAME,
                    preferenceService.getString(R.string.pkey_location),
                    preferenceService.getString(R.string.pkey_fcm_token_sent)).also {
                    subscribe(it.request){ data->
                        data.body()?.let {  response ->
                            if(response.status.code == 1){
                                CoroutineScope(Dispatchers.Main).launch {
                                    response.body.allowDailyPush?.let {
                                        preferenceService.putBoolean(R.string.pkey_allow_daily_push,it) }
                                    response.body.daily_message?.let {
                                        preferenceService.putString(R.string.pkey_daily_message,it)
                                    }
                                    preferenceService.putInt(R.string.pkey_notification_count,response.body.notification_count)
                                    notificationCount.postValue(response.body.notification_count)
                                    isNeedToUpdateDataFirstTime = false
                                    updateAdsList()
                                }
                            }
                        }
                    }
                }
            }
        }



    fun updateNotificationCounter(){
        notificationCount.value =  preferenceService.getInt(R.string.pkey_notification_count)
    }

    fun updateAdsList(){
        if(isNeedToUpdateAdsList && !preferenceService.getString(R.string.pkey_location).isNullOrEmpty()){
            country.postValue(preferenceService.getString(R.string.pkey_location))
        }
    }

    private var getAdsResource = ResourceViewModel(country) { country ->
        productRepository.getAdsList(preferenceService.getString(R.string.pkey_user_Id),country)
    }

    var adsList = getAdsResource.data.map {
       it.body
    }

    var adsNetworkState  = getAdsResource.networkState.map {
        it
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
            preferenceService.getString(R.string.pkey_userlat).isNullOrEmpty() -> locationRepository.getUserCurrentLocation {
                search.postValue(searchText)
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