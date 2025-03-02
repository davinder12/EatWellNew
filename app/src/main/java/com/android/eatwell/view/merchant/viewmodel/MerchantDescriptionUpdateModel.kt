package com.android.eatwell.view.merchant.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.MerchantRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject


@HiltViewModel
class MerchantDescriptionUpdateModel @Inject constructor(
        private val preferenceService: PreferenceService,
        private val merchantRepository: MerchantRepository
) : BaseViewModel() {

    val description = mutableLiveData("")



    fun updateMerchantDescription(): LiveData<NetworkState> {
        return merchantRepository.updateMerchantDescriptionMethod(preferenceService.getString(R.string.pkey_user_Id), description.value).also { subscribe(it.request) }.networkState
    }


    fun updateData(description: String) {
        this.description.value = description
    }


}