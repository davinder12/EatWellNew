package com.android.mealpass.view.merchant.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.merchant.adapter.MerchantNotificationAdapter
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.IS_SHOP_OPEN
import com.android.mealpass.view.merchant.fragment.MerchantPortion.Companion.RETAIL_PRICE
import com.android.mealpass.view.merchant.viewmodel.MerchantNotificationModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentMerchantNotificationBinding


@AndroidEntryPoint
class MerchantNotification : BaseListFragment<FragmentMerchantNotificationBinding>() {


    override val layoutRes: Int get() = R.layout.fragment_merchant_notification

    private val viewModel: MerchantNotificationModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  viewModel.updateItem(args.productItem)
        viewModel.callApi()
        backStackResponseData()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        initAdapter(MerchantNotificationAdapter(), binding.displayNotification, viewModel.notificationList)


        subscribe(binding.updatePortion.throttleClicks()) {
            findNavController().navigate(MerchantNotificationDirections.
            actionMerchantNotificationToMerchantPortion(viewModel.merchantNotificationResponse))
        }

        subscribe(binding.updateDescription.throttleClicks()) {
            findNavController().navigate(MerchantNotificationDirections.
            actionMerchantNotificationToMerchantDescriptionUpdate(viewModel.description))

        }
    }

    private fun backStackResponseData() {
        backStackGetIntData(IS_SHOP_OPEN)?.observe(viewLifecycleOwner,  {
            it?.let { viewModel.merchantNotificationResponse?.is_open = it }
        })
        backStackGetData(BACK_STACK_DESCRIPTION)?.observe(viewLifecycleOwner,  {
            it?.let {  viewModel.description = it }
        })
        backStackGetFloatData(BACK_STACK_DOUBLE)?.observe(viewLifecycleOwner,  {
            it?.let { viewModel.merchantNotificationResponse?.cost_price = it }
        })
        backStackGetFloatData(RETAIL_PRICE)?.observe(viewLifecycleOwner,  {
            it?.let { viewModel.merchantNotificationResponse?.retail_price = it }
        })
        backStackGetIntData(BACK_STACK_INT)?.observe(viewLifecycleOwner,  {
            Log.e("portion",""+it)
        })
    }

    override fun onBindView(binding: FragmentMerchantNotificationBinding) {
        binding.vm = viewModel
    }
}
