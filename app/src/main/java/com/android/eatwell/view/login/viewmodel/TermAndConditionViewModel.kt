package com.android.eatwell.view.login.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.extension.mutableLiveData
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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