package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityStaffMemberBinding
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
