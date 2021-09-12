package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.units.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UsedReceiptDetailViewModel @Inject constructor() : BaseViewModel() {


    var latitude =""
    var longitude =""

    var portion = mutableLiveData("")

    var usedReceiptData = MutableLiveData<ReceiptResponse.Body.UsedReceipt>()

    var storeUrl = usedReceiptData.map {
        it.logo
    }

    var storeAddress = usedReceiptData.map {
        it.address
    }


    var date = usedReceiptData.map {
        getDate(it.created_date)
    }

    var collectionTime = usedReceiptData.map {
         it.collection_time
    }

    var storeName = usedReceiptData.map {
        it.storename
    }


    var receiptId = usedReceiptData.map {
        it.receipt_id
    }


    var quantity = usedReceiptData.map {
        it.quantity
    }

    var currencyType = usedReceiptData.map {
        it.currency_type
    }

    var amount = usedReceiptData.map {
        it.amount
    }

    var isHomeDelivery = usedReceiptData.map {
        false
    }

    var isDelivery = usedReceiptData.map {
        it.isdelivery
    }

    var isDonateAmount = usedReceiptData.map {
        it.donated_amount.roundToInt() > 0
    }

    var donatedAmount = usedReceiptData.map {
        it.donated_amount.toString() + " " + it.currency_type
    }


    fun updateUsedData(
        data: ReceiptResponse.Body.UsedReceipt,
        portion: String
    ) {
        data.latitude?.let { latitude = it.toString() }
        data.longitude?.let { longitude = it.toString() }
        usedReceiptData.postValue(data)
        this.portion.value = portion

    }


}