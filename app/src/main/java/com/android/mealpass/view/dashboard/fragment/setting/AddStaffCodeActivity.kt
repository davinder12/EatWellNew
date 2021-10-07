package com.android.mealpass.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityStaffMemberBinding
import javax.inject.Inject

@AndroidEntryPoint
class AddStaffCodeActivity : DataBindingActivity<ActivityStaffMemberBinding>() {


    @Inject
    lateinit var navigationScreen: NavigationScreen


    private val viewModel: ReferralCodeViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.activity_staff_member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(binding.varify.throttleClicks()) {
            viewModel.filterMethod { status, message ->
                when {
                    status -> {
                        bindNetworkState(viewModel.callStaffCodeApi(), progressDialog(R.string.referral_code)) {
                            showMessage(getString(R.string.CongratulationReferralApproved))
                            onBackPressed()
                        }
                    }
                    else -> showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                }
            }
        }

        subscribe(binding.cross.throttleClicks()) {
            onBackPressed()
        }
    }

    override fun onBindView(binding: ActivityStaffMemberBinding) {
        binding.vm = viewModel
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }

    override fun onBackPressed() {
        when {
            requireReferralVisit() -> {
                navigationScreen.goToDashBoard()
                finish()
            }
            else -> finish()
        }
    }

}
