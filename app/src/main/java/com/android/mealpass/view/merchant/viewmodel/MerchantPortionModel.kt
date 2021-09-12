package com.android.mealpass.view.merchant.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mediatorLiveData
import com.android.mealpass.data.extension.switchMap
import com.android.mealpass.data.models.CommonResponseModel
import com.android.mealpass.data.models.MerchantNotificationResponse
import com.android.mealpass.data.network.IRequest
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.MerchantRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.merchant.fragment.MerchantPortion
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.SHOP_CLOSE
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.SHOP_OPEN
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MerchantPortionModel @Inject constructor(
        private val preferenceService: PreferenceService,
        private val merchantRepository: MerchantRepository
) : BaseViewModel() {


    var soldPortion = 0

    var productData : MutableLiveData<MerchantNotificationResponse.Body>? = MutableLiveData<MerchantNotificationResponse.Body>()

    var updateNotification = MutableLiveData<Boolean>()

    var retailPrice = productData?.map { it?.retail_price?.toString() } as MutableLiveData<String?>

    var currency = productData?.map { it?.currency_type }

    var costPrice = productData?.map { it?.protion_price?.toString() } as MutableLiveData<String?>

    var remainingPortion  = productData?.map {
        soldPortion = it.sold_portion
        it?.protion?.minus(it.sold_portion) } as MutableLiveData<Int?>

    var isOpen = productData?.map { it.is_open } as MutableLiveData<Int?>


    fun updateMerchantPortion(): LiveData<NetworkState> {
        return merchantRepository.updatePortionMethod(preferenceService.getString(R.string.pkey_user_Id), remainingPortion.value,
        TimeZone.getDefault().id).also { subscribe(it.request) {
            if(it.isSuccessful){
                updateNotification.postValue(it.isSuccessful)
            } }
        }.networkState
    }

    var updatePortionNotificationMethod =  updateNotification.switchMap {
        val portion =  remainingPortion.value?.plus(soldPortion)
        merchantRepository.portionPriceNotificationMethod(
                preferenceService.getString(R.string.pkey_user_Id),"",currency?.value,"1",portion).also {
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


    fun updateRetailPriceOrCostPrice(retailPrice:String?,costPrice:String?): LiveData<NetworkState> {
        return merchantRepository.updateRetailAndCostPriceMethod(
                preferenceService.getString(R.string.pkey_user_Id, ""),
                retailPrice,costPrice,currency?.value,TimeZone.getDefault().id,
        ).also {
            subscribe(it.request)
        }.networkState
    }


    fun incrementUpdate() {remainingPortion.value = remainingPortion.value?.inc()}

    fun decrementUpdate() {
        if(remainingPortion.value?: MerchantPortion.PORTION_LIMIT > MerchantPortion.PORTION_LIMIT) {
            remainingPortion.value = remainingPortion.value?.dec()
        }
    }

    fun updateData(merchantNotification: MerchantNotificationResponse.Body?) {
        merchantNotification?.let {
            this.productData?.value = merchantNotification
        }
    }


}

