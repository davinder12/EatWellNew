package com.android.mealpass.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.activity.dialog.AddFamilyMember
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import com.android.mealpass.widgets.messageDialog
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
                        bindNetworkState(viewModel.callRerralCodeApi(), progressDialog(R.string.referral_code)) {
                            val familyMember = AddFamilyMember.create {
                                messageDialog(getString(R.string.CongratulationReferralApproved), false) {
                                    // showMessage(getString(R.string.CongratulationReferralApproved))
                                    //onBackPressed()
                                    navigationScreen.goToRestaurnatOptionActivity()
                                    finish()
                                }
                            }
                            familyMember.show(supportFragmentManager, familyMember.tag)
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
        when {
            requireReferralVisit() -> {
                navigationScreen.goToDashBoard()
                finish()
            }
            else -> finish()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }

}
