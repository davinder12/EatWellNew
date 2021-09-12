package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.ReceiptRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.units.getDate
import com.android.mealpass.view.units.pickUpTime
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ActiveReceiptDetailViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository) :
    BaseViewModel() {


    var resultCode = 0
    var foodReceipt = MutableLiveData<ReceiptResponse.Body.ActiveReceipt>()

    var latitude = ""
    var longitude =""


    fun redeemOrder(): LiveData<NetworkState> {
        return receiptRepository.redeemOrderMethod(foodReceipt.value?.receipt_id).also {
            subscribe(it.request)
        }.networkState
    }


    fun updateActiveReceiptItem(item: ReceiptResponse.Body.ActiveReceipt) {
        item.latitude?.let { latitude = it.toString() }
        item.longitude?.let { longitude = it.toString() }
        foodReceipt.value = item
    }

    var date = foodReceipt.map {
        getDate(it.created_date)
    }

    var time = foodReceipt.map {
        it.collection_time
    }

    var storeName = foodReceipt.map {
        it.storename
        // it.receipt_info.receipt_product_info.product_info.name
    }

    var storeLogo = foodReceipt.map {
        it.logo
    }

    var storeAddress = foodReceipt.map {
        it.address
    }

    var userName = foodReceipt.map {
        it.username
    }

    var quanitity = foodReceipt.map {
        it.quantity
    }

    var isDelivery = foodReceipt.map {
        it.isdelivery
    }

    var paidPrice = foodReceipt.map {
        "" + it.amount + " " + it.currency_type
    }


    var isDonateAmount = foodReceipt.map {
        it.donated_amount?.roundToInt()?:0 > 0
    }

    var donatedAmount = foodReceipt.map {
        it.donated_amount.toString() + " " + it.currency_type
    }


    var productId = foodReceipt.map {
        it.receipt_id
    }


}