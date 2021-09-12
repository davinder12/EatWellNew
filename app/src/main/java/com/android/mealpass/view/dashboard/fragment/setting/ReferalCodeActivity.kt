package com.android.mealpass.view.dashboard.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityReferalCodeBinding
import javax.inject.Inject

@AndroidEntryPoint
class ReferalCodeActivity : DataBindingActivity<ActivityReferalCodeBinding>() {


    @Inject
     lateinit var navigationScreen: NavigationScreen


    private val viewModel : ReferralCodeViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.activity_referal_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateReferralScreen(requireReferralVisit())

        subscribe(binding.varify.throttleClicks()) {
            viewModel.filterMethod { status, message ->
                when {
                    status -> {
                        bindNetworkState(viewModel.callRerralCodeApi(), progressDialog(R.string.referral_code), R.string.CongratulationReferralApproved) {
                            when {
                                requireReferralVisit()-> {
                                    navigationScreen.goToDashBoard()
                                    finish()
                                }
                                else-> finish()
                            }
                        }
                    }
                    else ->  showSnackMessage(resources.getString(message?:R.string.Unknown_msg))
                }
            }
        }

        subscribe(binding.skipLable.throttleClicks()) {
            navigationScreen.goToDashBoard()
            finish()
        }

        subscribe(binding.cross.throttleClicks()) {
            finish()
        }
    }

    override fun onBindView(binding: ActivityReferalCodeBinding) {
        binding.vm = viewModel
    }


    override fun onBackPressed() {
        if(binding.cross.visibility == View.VISIBLE){ finish() }
        else{ navigationScreen.goToDashBoard()
            finish() }
    }

}
