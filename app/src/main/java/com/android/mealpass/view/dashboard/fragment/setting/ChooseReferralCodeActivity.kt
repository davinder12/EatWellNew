package com.android.mealpass.view.dashboard.fragment.setting

import android.os.Bundle
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityChooseReferralScreenBinding
import javax.inject.Inject

@AndroidEntryPoint
class ChooseReferralCodeActivity : DataBindingActivity<ActivityChooseReferralScreenBinding>() {


    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.activity_choose_referral_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(binding.staffBtn.throttleClicks()) {
            navigationScreen.goToStaffCodeScreen(true)
            finish()
        }

        subscribe(binding.foodbankBtn.throttleClicks()) {
            navigationScreen.goToReferralCodeScreen(true)
            finish()
        }

        subscribe(binding.skiptBtn.throttleClicks()) {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        navigationScreen.goToDashBoard()
        finish()
    }

}
