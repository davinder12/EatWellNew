package com.android.eatwell.view.dashboard.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.models.ReceiptResponse
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.viewmodel.UsedReceiptDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityUsedReceiptDetailBinding
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class UsedReceiptDetail : DataBindingActivity<ActivityUsedReceiptDetailBinding>() {

    val viewModel: UsedReceiptDetailViewModel by viewModels()

    companion object {
        const val CAMPAIGN_OPEN = 1
    }

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.activity_used_receipt_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActiveFoodReceipt(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL)?.let { item ->
            val portion = getReceiptModel(
                item.quantity,
                item.currency_type,
                item.amount,
                item.isFromCampaign == CAMPAIGN_OPEN
            )
            viewModel.updateUsedData(item, portion)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


        binding.toolbar.menu.findItem(R.id.contain_info)?.let {
            it.setOnMenuItemClickListener {
                navigationScreen.goToGoogleMap(viewModel.latitude,viewModel.longitude)
                true
            }
        }

    }

    override fun onBindView(binding: ActivityUsedReceiptDetailBinding) {
        binding.vm = viewModel
    }




    // TODO isFromCampaign is not working
    private fun getReceiptModel(
        quanitity: Int,
        currency: String?,
        amount: Float,
        isFromCampaign: Boolean
    ): String {
       // "" + quanitity + " " + getString(R.string.Portions) + " (" + currency + " " + amount + ")" + "(C)"
        return when {
            isFromCampaign -> "" + quanitity + " " + getString(R.string.Portions) + "(C)"
            amount.roundToInt() == 0 -> "" + quanitity + " " + getString(R.string.Portions) + " (" + getString(R.string.Free) + ")"
            !currency.isNullOrEmpty() -> "" + quanitity + " " + getString(R.string.Portions) + " (" + currency + " " + amount + ")"
            else -> "" + quanitity + " " + getString(R.string.Portions) + " (" + "AUD" + " " + amount + ")"
        }
    }

    private fun requireActiveFoodReceipt(tag: String): ReceiptResponse.Body.UsedReceipt? {
        var activeReceiptRespose: ReceiptResponse.Body.UsedReceipt? = null
        intent.getParcelableExtra<ReceiptResponse.Body.UsedReceipt>(tag)?.let {
            activeReceiptRespose = it
        }
        return activeReceiptRespose
    }

}
