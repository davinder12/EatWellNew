package com.android.eatwell.view.dashboard.fragment.setting.viewmodel


import androidx.lifecycle.LiveData
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.data.repository.UserRepository
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eatwell.com.eatwell.R
import javax.inject.Inject

@HiltViewModel
class FeedBackViewModel @Inject constructor(private val userRepository: UserRepository,
                                            private val preferenceService: PreferenceService) :
    BaseViewModel() {


     var subject : String = ""
     var message : String = ""


    fun updateFeedBack(): LiveData<NetworkState> {
        return userRepository.feedBackMethod(
                preferenceService.getString(R.string.pkey_user_Id),
                subject,
                message
        ).also {
            subscribe(it.request)
        }.networkState
    }


    fun filterMethod(callBack:(Boolean,Int?)->Unit){
        when{
            subject.isEmpty() -> callBack(false,R.string.FeedBackValidation)
            message.isEmpty() -> callBack(false,R.string.FeedBackValidation)
            else -> callBack(true,null)
        }
    }


}


