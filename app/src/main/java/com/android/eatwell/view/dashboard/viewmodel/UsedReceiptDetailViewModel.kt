package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import com.android.eatwell.data.extension.map
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.models.ReceiptResponse
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import com.android.eatwell.view.units.getDate
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


    var storeName = usedReceiptData.map {
        it.storename
    }


    var receiptId = usedReceiptData.map {
        it.receipt_id
    }


    var quantity = usedReceiptData.map {
        it.quantity
    }


    var amount = usedReceiptData.map {
        it.amount
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

    var isOrderCancel = usedReceiptData.map {
        it.is_cancel
    }

    var isStaffReceipt = usedReceiptData.map {
        it.isStaffReceipt == 1
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