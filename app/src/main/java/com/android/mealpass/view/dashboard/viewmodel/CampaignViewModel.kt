package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.ReceiptRepository
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CampaignViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository
) : BaseViewModel() {


    fun updateReceiptRequest(saveReceiptRequestModel: SaveReceiptRequestModel): LiveData<NetworkState> {
        return receiptRepository.campaignReceiptMethod(saveReceiptRequestModel).also {
            subscribe(it.request)
        }.networkState
    }


}