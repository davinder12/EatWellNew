package com.android.mealpass.view.merchant.viewmodel


import androidx.lifecycle.*
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.models.MerchantNotificationResponse
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.MerchantRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.merchant.fragment.MerchantPortion
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.SHOP_CLOSE
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.SHOP_OPEN
import com.android.mealpass.view.units.DataChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MerchantPortionModel @Inject constructor(
        private val preferenceService: PreferenceService,
        private val merchantRepository: MerchantRepository
) : BaseViewModel() {


    var soldPortion = 0

    var isPortionUpdated = false


    var productData: MutableLiveData<MerchantNotificationResponse.Body>? = MutableLiveData<MerchantNotificationResponse.Body>()

    var updateNotification = MutableLiveData<Boolean>()

    var retailPrice = productData?.map { it?.retail_price?.toString() } as MutableLiveData<String?>

    var currency = productData?.map { it?.currency_type }

    var costPrice = productData?.map { it?.protion_price?.toString() } as MutableLiveData<String?>


    var totalPortion = productData?.map {
        soldPortion = it.sold_portion
        it?.protion
    } as MutableLiveData<Int?>

    var monthlyTaxDeduction = productData?.map {
        minOf(it.cost_price.times(2), (it.retail_price.minus(it.cost_price).div(2).plus(it.cost_price))).times(it.protion)
    } as MutableLiveData<Float>

    var yearlyTaxDeduction = productData?.map {
        minOf(it.cost_price.times(2), (it.retail_price.minus(it.cost_price).div(2).plus(it.cost_price))).times(it.protion)
                .run { this * 365 }
    } as MutableLiveData<Float>

    var isOpen = productData?.map { it.is_open } as MutableLiveData<Int?>


    fun updateMerchantPortion(): LiveData<NetworkState> {
        return merchantRepository.updatePortionMethod(preferenceService.getString(R.string.pkey_user_Id), totalPortion.value,
                TimeZone.getDefault().id).also {
            subscribe(it.request) {
                if (it.isSuccessful) {
                    updateNotification.postValue(it.isSuccessful)
                }
            }
        }.networkState
    }

    var updatedCondition = DataChangeListener(retailPrice, costPrice, totalPortion).switchMap {
        val retailPrice = it.first?.toFloatOrNull() ?: 0.0f
        val costPrice = it.second?.toFloatOrNull() ?: 0.0f
        val totalPortion = it.third ?: 0
        return@switchMap liveData {
            emit(minOf(costPrice.times(2), (retailPrice.minus(costPrice).div(2).plus(costPrice))).times(totalPortion))
        }
    }


    fun callProductDetailApi(): LiveData<NetworkState> {
        return merchantRepository.updateProductDetailMethod(
                preferenceService.getString(R.string.pkey_user_Id, ""),
                totalPortion.value, retailPrice.value, costPrice.value, currency?.value, TimeZone.getDefault().id,
        ).also {
            subscribe(it.request) {
                if (it.isSuccessful && isPortionUpdated && totalPortion.value ?: 0 > soldPortion) {
                    updateNotification.postValue(it.isSuccessful)
                }
            }
        }.networkState
    }

    var updatePortionNotificationMethod = updateNotification.switchMap {
        val portion = totalPortion.value
        merchantRepository.portionPriceNotificationMethod(
                preferenceService.getString(R.string.pkey_user_Id), "", currency?.value, "1", portion).also {
            subscribe(it.request)
        }.networkState
    }

    fun updateResturantStatus(): LiveData<NetworkState> {
        val status = if(isOpen.value == SHOP_OPEN) SHOP_CLOSE else SHOP_OPEN
        return merchantRepository.changeResturantStatusMethod(
                preferenceService.getString(R.string.pkey_user_Id),status).also { subscribe(it.request) {
            if(it.isSuccessful){ isOpen.postValue(status)
            }
                }
        }.networkState
    }


    fun updateRetailPriceOrCostPrice(retailPrice: String?, costPrice: String?): LiveData<NetworkState> {
        return merchantRepository.updateRetailAndCostPriceMethod(
                preferenceService.getString(R.string.pkey_user_Id, ""),
                retailPrice, costPrice, currency?.value, TimeZone.getDefault().id,
        ).also {
            subscribe(it.request)
        }.networkState
    }


    fun incrementUpdate() {
        isPortionUpdated = true
        totalPortion.value = totalPortion.value?.inc()
    }

    fun decrementUpdate(callBack: (Boolean) -> Unit) {
        isPortionUpdated = true
        if (totalPortion.value ?: MerchantPortion.PORTION_LIMIT > soldPortion) {
            totalPortion.value = totalPortion.value?.dec()
        } else {
            callBack.invoke(true)
        }
    }

    fun updateData(merchantNotification: MerchantNotificationResponse.Body?) {
        merchantNotification?.let {
            this.productData?.value = merchantNotification
        }
    }

    fun filterMethod(callBack: (Boolean) -> Unit) {
        when {
            totalPortion.value ?: 0 <= 0 || retailPrice.value.isNullOrEmpty() || costPrice.value.isNullOrEmpty() -> callBack(false)
            else -> callBack(true)
        }
    }


}

