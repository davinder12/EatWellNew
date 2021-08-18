package com.android.mealpass.view.dashboard.fragment.receipt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.activity.ActiveReceiptDetail
import com.android.mealpass.view.dashboard.adapter.ActiveReceiptAdapter
import com.android.mealpass.view.dashboard.viewmodel.ReceiptFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentReceiptItemBinding
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

            }
        }

          initAdapter(ActiveReceiptAdapter(), binding.receiptList, viewModel.activeReceiptList,viewModel.networkState) { activeReceiptData ->
              Intent(requireActivity(), ActiveReceiptDetail::class.java).let {
                  it.putExtra(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL, activeReceiptData)
                  launchSomeActivity.launch(it)
              }



//              navigationScreen.goToDeliveryScreen()
//              launchSomeActivity.launch(Intent(requireActivity(), ActiveReceiptDetail::class.java))
//
//              val intent = Intent(requireActivity(), ActiveReceiptDetail::class.java)
//            intent.putExtra(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL, it)
//            startActivityForResult(intent, REQUEST_CODE)
        }
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) viewModel.updateReceipt()
//    }
}
