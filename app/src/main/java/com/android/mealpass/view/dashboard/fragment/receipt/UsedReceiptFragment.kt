package com.android.mealpass.view.dashboard.fragment.receipt

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.adapter.UsedReceiptAdapter
import com.android.mealpass.view.dashboard.viewmodel.ReceiptFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_receipt_item.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentReceiptItemBinding
import javax.inject.Inject


@AndroidEntryPoint
class UsedReceiptFragment : BaseListFragment<FragmentReceiptItemBinding>() {

    override val layoutRes: Int get() = R.layout.fragment_receipt_item

    @Inject
    lateinit var  navigationScreen: NavigationScreen

    val viewModel : ReceiptFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter(UsedReceiptAdapter(), receiptList, viewModel.usedReceiptList,viewModel.networkState) {
            navigationScreen.goToUsedReceiptDetailActivity(it)
        }
    }

    fun updateList(){
        viewModel.updateReceipt()

    }




}
