package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.models.FoodData
import com.android.mealpass.data.models.FoodRequestModel
import com.android.mealpass.data.repository.LocationRepository
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.PagedListViewModel
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.CURRENT_LIST_SIZE
import com.android.mealpass.view.dashboard.fragment.HomeFragment.Companion.OFFSET
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
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
                preferenceService.getString(R.string.pkey_userlat),
                CURRENT_LIST_SIZE,
                preferenceService.getString(R.string.pkey_userLong),
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
        it.body
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