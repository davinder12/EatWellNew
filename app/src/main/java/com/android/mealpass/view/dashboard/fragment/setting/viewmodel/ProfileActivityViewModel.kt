package com.android.mealpass.view.dashboard.fragment.setting.viewmodel


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.UserRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.dashboard.fragment.setting.ProfileActivity.Companion.ITEM_NOT_SELECTED
import com.android.mealpass.view.dashboard.fragment.setting.ProfileActivity.Companion.ITEM_SELECTED
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileActivityViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val preferenceService: PreferenceService
) :
    BaseViewModel() {


    var userId = MutableLiveData<String>()

    var isEditTable = mutableLiveData(false)

    private val profileResponse = ResourceViewModel(userId) {
        userRepository.getProfileData(it)
    }

    var networkState = profileResponse.networkState.map {
        it
    }

    var name  = profileResponse.data.map { it.body.name } as MutableLiveData<String?>

    var emailId = profileResponse.data.map { it.body.email } as MutableLiveData<String?>

    var phoneNumber = profileResponse.data.map { it.body.mobile } as MutableLiveData<String?>

    var imageUrl = profileResponse.data.map { it.body.user_image } as MutableLiveData<String?>

    var newsLetter = profileResponse.data.map {
        it.body.newsletter
    }


    fun getProfileData() {
        userId.postValue(preferenceService.getString(R.string.pkey_user_Id))
    }

    fun updateEditableValue() {
        isEditTable.value?.let {
            isEditTable.value = !it
        }
    }

    fun updateProfilePic(url: String) {
        imageUrl.value = url
    }




    fun uploadProfileData(isNewsLetterCheck: Boolean): LiveData<NetworkState> {

//        imageUrl.value?.let {
//            if(it.isNotEmpty() && !it.contains("http"))
//                map["user_image"] = File(it).asRequestBody(UserRepository.FILE_FORMAT.toMediaTypeOrNull())
//        }

        val networkRequest = userRepository.updateProfileData(profileRequestModel(isNewsLetterCheck),imageUrl.value)
        subscribe(networkRequest.request){
            if(it.isSuccessful && it.body()?.status?.code == 1){
                updateProfileInformation()
            }
        }
        return networkRequest.networkState
    }

    private fun updateProfileInformation(){
        preferenceService.putString(R.string.pkey_phoneNumber, phoneNumber.value)
        if(preferenceService.getBoolean(R.string.pkey_social_login))
        { preferenceService.putString(R.string.pkey_social_emaiId, emailId.value) }
        else { preferenceService.putString(R.string.pkey_emaiId, emailId.value) }
    }

    private fun profileRequestModel(isNewsLetterCheck:Boolean): HashMap<String, RequestBody?> {
        val map: HashMap<String, RequestBody?> = HashMap()
        map["user_id"] = createPartFromString(preferenceService.getString(R.string.pkey_user_Id))
        map["mobile"] = createPartFromString(phoneNumber.value)
        map["name"] = createPartFromString(name.value)
        map["newsletter"] = createPartFromString(if (isNewsLetterCheck) ITEM_SELECTED else ITEM_NOT_SELECTED)
        map["email"]= createPartFromString(emailId.value)
        map["app_version"] = createPartFromString(BuildConfig.VERSION_NAME)
        return map
    }

    private fun createPartFromString(stringData: String?): RequestBody? {
        return stringData?.toRequestBody(UserRepository.PLAIN_TEXT.toMediaTypeOrNull())
    }


    fun filterMethod(callBack: (Boolean, Int?) -> Unit) {
        when {
            name.value.isNullOrEmpty() -> callBack(false, R.string.InvalidUsername)
            emailId.value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailId.value ?: "")
                .matches() -> callBack(false, R.string.EnterEmail)
            else -> callBack(true, null)
        }
    }

}


