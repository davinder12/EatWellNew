package com.android.mealpass.view.merchant.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.MerchantNotificationResponse
import com.android.mealpass.data.repository.MerchantRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.PagedListViewModel
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MerchantNotificationModel @Inject constructor(
        private val preferenceService: PreferenceService,
        merchantRepository: MerchantRepository
) : BaseViewModel() {

    private val userId = MutableLiveData<String>()

    var price = 0.0
    var portion = 0
    var description = ""

    var merchantNotificationResponse : MerchantNotificationResponse.Body? = null


    private val merchantResource = ResourceViewModel(userId) {
        merchantRepository.getPortionListMethod(it, TimeZone.getDefault().id)
    }

    val notificationList = merchantResource.data.map {
        merchantNotificationResponse = it.body
        description =  it.body.expected_description
        it.body.notifictions
    }


     fun callApi(){
        userId.value = preferenceService.getString(R.string.pkey_user_Id,"")
    }


//    private fun getMerchantList(productId: Int) {
//        val item = MerchantNotificationRequest(
//            5,
//            0,
//            productId,
//            preferenceService.getString(R.string.pkey_user_Id)
//        )
//        notificationRequestModel.postValue(item)
//    }

//    fun updateItem(productItem: MerchantNotificationResponse.Body) {
//        price = productItem.price
//        portion = productItem.portion
//        resturantName = productItem.storeName ?: ""
//        getMerchantList(productItem.product_id)
//    }


}