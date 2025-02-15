package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.eatwell.data.repository.ReceiptRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.ResourceViewModel
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject

@HiltViewModel
class ReceiptFragmentViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {

    var userId = MutableLiveData<String>()

     val receiptResponse = ResourceViewModel(userId) {
        receiptRepository.getReceiptMethod(it)
    }

    var activeReceiptList = receiptResponse.data.map {
        it.body.active_receipts
    }

    var usedReceiptList = receiptResponse.data.map {
        it.body.used_receipts
    }


    var networkState  = receiptResponse.networkState.map {
        it
    }




    fun updateReceipt() {
        userId.value = preferenceService.getString(R.string.pkey_user_Id)
    }

}