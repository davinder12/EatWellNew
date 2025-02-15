package com.android.eatwell.view.dashboard.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.models.ActiveReceiptMapper
import com.android.eatwell.data.models.ReceiptResponse
import com.android.eatwell.data.models.SaveReceiptRequestModel
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.ReceiptRepository
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CampaignViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository
) : BaseViewModel() {


    var storeName = mutableLiveData("")

    var response: ReceiptResponse.Body.ActiveReceipt? = null
    fun updateReceiptRequest(saveReceiptRequestModel: SaveReceiptRequestModel): LiveData<NetworkState> {
        return receiptRepository.campaignReceiptMethod(saveReceiptRequestModel).also {
            subscribe(it.request) {
                if (it.isSuccessful) {
                    response = ActiveReceiptMapper.Mapper.from(it.body()?.body)
                }
            }
        }.networkState
    }
}