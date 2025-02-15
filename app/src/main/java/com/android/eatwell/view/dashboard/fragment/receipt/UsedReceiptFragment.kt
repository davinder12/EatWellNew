package com.android.eatwell.view.dashboard.fragment.receipt

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.adapter.UsedReceiptAdapter
import com.android.eatwell.view.dashboard.viewmodel.ReceiptFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_receipt_item.*
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentReceiptItemBinding
import javax.inject.Inject


@AndroidEntryPoint
class UsedReceiptFragment : BaseListFragment<FragmentReceiptItemBinding>() {

    override val layoutRes: Int get() = R.layout.fragment_receipt_item

    @Inject
    lateinit var  navigationScreen: NavigationScreen

    val viewModel : ReceiptFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateReceipt()
        initAdapter(UsedReceiptAdapter(), receiptList, viewModel.usedReceiptList,viewModel.receiptResponse) {
            navigationScreen.goToUsedReceiptDetailActivity(it)
        }

        subscribe(emptyView.throttleClicks()) {
            updateList()
        }


    }

    fun updateList() {
        viewModel.updateReceipt()
    }




}
