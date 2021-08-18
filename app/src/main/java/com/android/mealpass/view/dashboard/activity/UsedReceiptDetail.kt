package com.android.mealpass.view.dashboard.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.viewmodel.UsedReceiptDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityUsedReceiptDetailBinding
import kotlin.math.roundToInt

@AndroidEntryPoint
class UsedReceiptDetail : DataBindingActivity<ActivityUsedReceiptDetailBinding>() {

    val viewModel: UsedReceiptDetailViewModel by viewModels()

    companion object {
        const val CAMPAIGN_OPEN = 1
    }

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

//        toolbar.setNavigationOnClickListener {
//            finish()
//        }
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
        return when {
            isFromCampaign  && !currency.isNullOrEmpty() -> "" + quanitity + " " + getString(R.string.Portions) + " (" + currency + " " + amount + ")" + "(C)"
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
