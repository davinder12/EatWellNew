package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.eatwell.data.models.FoodData
import com.android.eatwell.data.models.FoodRequestModel
import com.android.eatwell.data.repository.LocationRepository
import com.android.eatwell.data.repository.ProductRepository
import com.android.eatwell.data.repository.ProductRepository.Companion.RESTURANT_ACTIVE
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.ResourceViewModel
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.dashboard.fragment.HomeFragment.Companion.CURRENT_LIST_SIZE
import com.android.eatwell.view.dashboard.fragment.HomeFragment.Companion.OFFSET
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FavouriteFragmentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService,
    private val locationRepository: LocationRepository

) :
    BaseViewModel() {

    private val userId = MutableLiveData<String>()

    var favouriteItemList = ResourceViewModel(userId) {
        productRepository.favouriteListApi(
            FoodRequestModel(
                "",
                preferenceService.getString(R.string.pkey_userlat,"0.0"),
                CURRENT_LIST_SIZE,
                preferenceService.getString(R.string.pkey_userLong,"0.0"),
                OFFSET,
                TimeZone.getDefault().id,
                it
            )
        )
    }

    var networkState = favouriteItemList.networkState.map {
        it
    }

    var favouriteItemListData :LiveData<List<FoodData.Body>?>  = favouriteItemList.data.map {
        it.body?.filter { it.is_active == RESTURANT_ACTIVE }
    }

    fun updateLocation() {
        when {
            preferenceService.getString(R.string.pkey_userlat)
                .isNullOrEmpty() -> locationRepository.getUserCurrentLocation {
                this.userId.postValue(
                    preferenceService.getString(R.string.pkey_user_Id, "")
                )
            }
            else -> {
                this.userId.postValue(preferenceService.getString(R.string.pkey_user_Id, ""))
                locationRepository.getUserCurrentLocation()
            }
        }
    }
}