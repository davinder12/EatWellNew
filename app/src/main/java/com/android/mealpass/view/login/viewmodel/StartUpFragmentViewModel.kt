package com.android.mealpass.view.login.viewmodel


import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.login.fragment.StartUpFragment.Companion.EMAIL
import com.android.mealpass.view.login.fragment.StartUpFragment.Companion.FACEBOOK_ID
import com.android.mealpass.view.login.fragment.StartUpFragment.Companion.NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class StartUpFragmentViewModel @Inject constructor(private val preferenceService: PreferenceService) :
    BaseViewModel() {

    fun updateDeviceToken(deviceToken: String) {
        preferenceService.putString(R.string.pkey_fcm_token_sent, deviceToken)
    }

    var name = ""
    var emailId = ""
    var facebookId = ""


    fun getResponseFromFacebook(jsonObject: JSONObject, callBack: (Boolean, Int) -> Unit) {
        if (jsonObject.toString().contains(EMAIL)) {
            emailId = jsonObject.getString(EMAIL)
        }
        if (jsonObject.toString().contains(NAME)) {
            name = jsonObject.getString(NAME)
        }
        facebookId = jsonObject.getString(FACEBOOK_ID)
        when {
            facebookId.isEmpty() -> callBack(false, R.string.FailureMessage)
            else -> callBack(true, R.string.login_success)
        }
    }


}