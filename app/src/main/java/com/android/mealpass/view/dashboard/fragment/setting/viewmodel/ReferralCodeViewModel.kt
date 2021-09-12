package com.android.mealpass.view.dashboard.fragment.setting.viewmodel


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
class ReferralCodeViewModel @Inject constructor(private val userRepository: UserRepository,
                                                private val preferenceService: PreferenceService) :
    BaseViewModel(){

      var referralCode  = mutableLiveData("")
      var isFirstTimeVisit = mutableLiveData(false)

     fun callRerralCodeApi(): LiveData<NetworkState> {
      val emailId =  if(preferenceService.getBoolean(R.string.pkey_social_login)) { preferenceService.getString(R.string.pkey_social_emaiId)
      } else { preferenceService.getString(R.string.pkey_emaiId) }
     val networkRequest = userRepository.referralCodeMethod(preferenceService.getString(R.string.pkey_user_Id),
         emailId,referralCode.value)
         subscribe(networkRequest.request)
      return networkRequest.networkState
    }


    fun filterMethod(callBack:(Boolean,Int?)->Unit){
        when{
            referralCode.value.isNullOrEmpty()-> callBack(false,R.string.referral_code_validation)
            else -> callBack(true,null)
        }
    }

    fun updateReferralScreen(item :Boolean){
        isFirstTimeVisit.value = item
    }

}