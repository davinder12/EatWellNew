package com.android.mealpass.view.dashboard.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.dashboard.viewmodel.CampaignViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityCampaignCodeBinding

@AndroidEntryPoint
class Campaign : DataBindingActivity<ActivityCampaignCodeBinding>() {


    override val layoutRes: Int get() = R.layout.activity_campaign_code

    private val viewModel: CampaignViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        subscribe(binding.getmagicPortion.throttleClicks()) {
            requirePaymentRequest()?.let {
                bindNetworkState(
                    viewModel.updateReceiptRequest(it),
                    progressDialog(R.string.Pleasewait)
                ) {
                    finish()
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

}
