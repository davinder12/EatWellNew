package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.repository.ReceiptRepository
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class ReceiptFragmentViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val preferenceService: PreferenceService
) :
    BaseViewModel() {

    var userId = MutableLiveData<String>()

    private val receiptResponse = ResourceViewModel(userId) {
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