package com.android.mealpass.view.login.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import javax.inject.Inject

@HiltViewModel
class TermAndConditionViewModel @Inject constructor(private val userRepository: UserRepository
) :
    BaseViewModel() {

     var data  = mutableLiveData("")

    fun callTermConditionMethod(isMerchantLogin :String?): LiveData<NetworkState> {
        return userRepository.termConditionMethod(isMerchantLogin).also {
            subscribe(it.request) { response ->
                response.body()?.let {
                    data.postValue(it.body.terms)
                }
            }
        }.networkState
    }

}