package com.android.eatwell.view.dashboard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.data.models.ReceiptResponse
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.viewmodel.ActiveReceiptDetailViewModel
import com.android.eatwell.widgets.SlideToUnlock
import com.android.eatwell.widgets.alertDialog
import com.android.eatwell.widgets.messageDialog
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityActiveReceiptDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class ActiveReceiptDetail : DataBindingActivity<ActivityActiveReceiptDetailBinding>(),
    SlideToUnlock.OnUnlockListener {

    val viewModel: ActiveReceiptDetailViewModel by viewModels()

      @Inject
      lateinit var  navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.activity_active_receipt_detail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.slidetounlock1.setOnUnlockListener(this)

        requireActiveFoodReceipt()?.let {
            viewModel.updateActiveReceiptItem(it)
        }


        binding.toolbar.menu.findItem(R.id.right_dirction)?.let {
            it.setOnMenuItemClickListener {
                navigationScreen.goToGoogleMap(viewModel.latitude, viewModel.longitude)
                true
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        subscribe(binding.cancelOrderBtn.throttleClicks()) {
            viewModel.filterMethod { condition, message ->
                when {
                    condition -> cancelOrderMethod()
                    else -> showSnackMessage(getString(message ?: R.string.Unknown_msg))
                }
            }
        }
    }


    private fun cancelOrderMethod() {
        alertDialog(
                getString(R.string.Confirm),
                getString(R.string.CancelOrderConfirmation),
                getString(R.string.Yes),
                getString(R.string.No),
                successResponse = {
                    bindNetworkState(viewModel.cancelOrder(), progressDialog(R.string.Pleasewait), onSuccess = {
                        messageDialog(getString(R.string.CancelOrderMessage), false) {
                            viewModel.resultCode = Activity.RESULT_OK
                            onBackPressed()
                        }
                    })
                })
    }

    override fun onBackPressed() {
        when {
            requireIsPaymentComplete() -> {
                navigationScreen.goToDashBoard()
                finish()
            }
            else -> {
                Intent().also { setResult(viewModel.resultCode, it) }
                finish()
            }
        }
    }

    override fun onBindView(binding: ActivityActiveReceiptDetailBinding) {
        binding.vm = viewModel
    }


    override fun onUnlock() {
        alertDialog(
            getString(R.string.Confirm),
            getString(R.string.RedeemReceipt),
            getString(R.string.Continue),
            getString(R.string.Cancel),
            successResponse = {
                bindNetworkState(
                    viewModel.redeemOrder(),
                    progressDialog(R.string.Pleasewait),
                    onSuccess = {
                        binding.slidetounlock1.reset()
                        binding.purchase.visibility = View.VISIBLE
                        viewModel.resultCode = Activity.RESULT_OK
                    },
                    onError = {
                        binding.slidetounlock1.reset()
                    })
            },
            errorResponse = {
                binding.slidetounlock1.reset()
            })
    }

    private fun requireActiveFoodReceipt(): ReceiptResponse.Body.ActiveReceipt? {
        var activeReceiptRespose: ReceiptResponse.Body.ActiveReceipt? = null
        intent.getParcelableExtra<ReceiptResponse.Body.ActiveReceipt>(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL)?.let {
            activeReceiptRespose = it
        }
        return activeReceiptRespose
    }


}