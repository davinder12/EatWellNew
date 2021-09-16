package com.android.mealpass.view.dashboard.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.CLOSE_RESTURANT
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.MAX_TIME
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.MAX_TIME_LABEL
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.MIN_TIME
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.MIN_TIME_LABEL
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter.Companion.OPEN_RESTURANT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FoodFilterViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService
) : BaseViewModel() {


    var minimumTime =
        mutableLiveData(preferenceService.getString(R.string.pkey_fromTime, MIN_TIME_LABEL))
    var maxTime = mutableLiveData(preferenceService.getString(R.string.pkey_toTime, MAX_TIME_LABEL))
    var isShowingOpenStore =
        mutableLiveData(
            preferenceService.getString(
                R.string.pkey_showOpenResturnat,
                CLOSE_RESTURANT
            )?.run {
                this.equals("1", ignoreCase = true)
            })


    var userId = MutableLiveData<String>()
    var minValueInit = MIN_TIME
    var maxValueInit = MAX_TIME

    init {
        userId.postValue(preferenceService.getString(R.string.pkey_user_Id))
        minValueInit = preferenceService.getFloat(R.string.pkey_minValue, MIN_TIME)
        maxValueInit = preferenceService.getFloat(R.string.pkey_maxValue, MAX_TIME)
    }

    var resource = ResourceViewModel(userId) {
        productRepository.getFoodList(it, getListOfObject())
    }

    var dataList = resource.data.map { it.body }


    fun onCheckedChanged(checked: Boolean) {
        isShowingOpenStore.value = checked
    }


    fun seekBarChangeListener(minValue: Number, maxValue: Number) {
        val toMinute = minValue.toLong() % 60
        val toHours = minValue.toLong() / 60
        val fromMinute = maxValue.toLong() % 60
        val fromHour = maxValue.toLong() / 60
        minimumTime.value = String.format("%02d:%02d", toHours, toMinute)
        maxTime.value = String.format("%02d:%02d", fromHour, fromMinute)
        minValueInit = minValue.toFloat()
        maxValueInit = maxValue.toFloat()

    }


    fun saveFilterRecord() {
        val filteredFood = dataList.value?.filter { it.isItemSelected }?.map { it.id }
        preferenceService.putString(R.string.pkey_fromTime, minimumTime.value)
        preferenceService.putString(R.string.pkey_toTime, maxTime.value)
        preferenceService.putFloat(R.string.pkey_minValue, minValueInit)
        preferenceService.putFloat(R.string.pkey_maxValue, maxValueInit)
        preferenceService.putString(R.string.pkey_showOpenResturnat,
            if (isShowingOpenStore.value == true) OPEN_RESTURANT else CLOSE_RESTURANT
        )
        filteredFood?.let {
            preferenceService.putString(R.string.pkey_filteredFoodList, Gson().toJson(filteredFood))
        }

    }

    private fun getListOfObject(): List<String> {
        var response = listOf<String>()
        try{
            preferenceService.getString(R.string.pkey_filteredFoodList)?.let {
                if(it.isNotEmpty()) {
                    val type = object : TypeToken<ArrayList<String>>() {}.type
                    response = Gson().fromJson(it, type)
                }
            }
        }catch (e:Exception){}
        return response
    }


}