package com.android.mealpass.view.dashboard.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.viewmodel.CampaignViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityCampaignCodeBinding
import javax.inject.Inject

@AndroidEntryPoint
class Campaign : DataBindingActivity<ActivityCampaignCodeBinding>() {


    override val layoutRes: Int get() = R.layout.activity_campaign_code

    private val viewModel: CampaignViewModel by viewModels()

    @Inject
    lateinit var navigationScreen: NavigationScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        subscribe(binding.getmagicPortion.throttleClicks()) {
            requirePaymentRequest()?.let {
                bindNetworkState(
                    viewModel.updateReceiptRequest(it),
                    progressDialog(R.string.Pleasewait)
                ) {
                    viewModel.response?.let { data ->
                       startActivity(navigationScreen.goToActiveReceipt(data,true))
                    }
                    finish()
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

}
