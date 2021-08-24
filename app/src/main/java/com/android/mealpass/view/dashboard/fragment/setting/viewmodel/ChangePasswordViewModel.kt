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
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository,
                                                  private val preferenceService: PreferenceService) :
    BaseViewModel() {


     var oldPassword : String = ""
     var newPassword : String = ""
     var confirmPassword: String = ""

    var isSocialLogin  = mutableLiveData(preferenceService.getBoolean(R.string.pkey_social_login))


    fun changePasswordApi(): LiveData<NetworkState> {
        return userRepository.updatePasswordMethod(
                preferenceService.getString(R.string.pkey_user_Id),
                oldPassword,
                newPassword
        ).also {
            subscribe(it.request)
        }.networkState
    }


    fun filterMethod(callBack:(Boolean,Int?)->Unit){
        when{
            oldPassword.isEmpty() ||  oldPassword.length < 5 -> callBack(false,R.string.PasswordLimit)
            newPassword.isEmpty() ||  newPassword.length < 5 -> callBack(false,R.string.PasswordLimit)
            !newPassword.equals(confirmPassword,ignoreCase = true)-> callBack(false,R.string.PasswordMismatch)
            else -> callBack(true,null)
        }
    }


}


