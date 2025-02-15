
package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityChooseReferralScreenBinding
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
