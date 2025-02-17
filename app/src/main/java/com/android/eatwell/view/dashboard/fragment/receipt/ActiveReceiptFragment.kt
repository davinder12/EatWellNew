package com.android.eatwell.view.dashboard.fragment.receipt

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.adapter.ActiveReceiptAdapter
import com.android.eatwell.view.dashboard.viewmodel.ReceiptFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentReceiptItemBinding
import javax.inject.Inject


@AndroidEntryPoint
class ActiveReceiptFragment : BaseListFragment<FragmentReceiptItemBinding>() {

    override val layoutRes: Int get() = R.layout.fragment_receipt_item

    val viewModel: ReceiptFragmentViewModel by viewModels()



    @Inject
    lateinit var navigationScreen: NavigationScreen

    companion object {
        const val REQUEST_CODE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateReceipt()

        val launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.updateReceipt()
            }
        }

        subscribe(emptyView.root.throttleClicks()) {
            viewModel.updateReceipt()
        }


        initAdapter(ActiveReceiptAdapter(), binding.receiptList, viewModel.activeReceiptList,viewModel.receiptResponse) { activeReceiptData ->
            launchSomeActivity.launch(navigationScreen.goToActiveReceipt(activeReceiptData))
        }
    }


}
