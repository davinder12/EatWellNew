package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.activity.dialog.AddFamilyMember
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import com.android.eatwell.widgets.messageDialog
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityReferalCodeBinding
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
