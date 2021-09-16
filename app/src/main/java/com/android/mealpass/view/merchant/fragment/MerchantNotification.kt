package com.android.mealpass.view.merchant.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.service.MealPassFirebaseMessagingService
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.fragment.dialog.SignOutDialog
import com.android.mealpass.view.merchant.MerchantActivity
import com.android.mealpass.view.merchant.adapter.MerchantNotificationAdapter
import com.android.mealpass.view.merchant.viewmodel.MerchantNotificationModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentMerchantNotificationBinding
import javax.inject.Inject


@AndroidEntryPoint
class MerchantNotification : BaseListFragment<FragmentMerchantNotificationBinding>() {

     @Inject
     lateinit var navigationScreen : NavigationScreen

    override val layoutRes: Int get() = R.layout.fragment_merchant_notification

    private val viewModel: MerchantNotificationModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.callApi()
        updateList()
      //  backStackResponseData()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val adapter =  initAdapter(MerchantNotificationAdapter(), binding.displayNotification, viewModel.notificationList,viewModel.merchantResource)

        viewModel.networkState.observe(viewLifecycleOwner, {
            adapter.setNetworkState(it)
            if(it == NetworkState.success && viewModel.statusCode != 1){
              viewModel.updateMerchantToken()
            }
        })

        subscribe(binding.updatePortion.throttleClicks()) {
            findNavController().navigate(MerchantNotificationDirections.actionMerchantNotificationToMerchantPortion(viewModel.merchantNotificationResponse))
        }

        subscribe(binding.updateDescription.throttleClicks()) {
            findNavController().navigate(MerchantNotificationDirections.actionMerchantNotificationToMerchantDescriptionUpdate(viewModel.description))

        }
        binding.toolbar.setNavigationOnClickListener {
            val signOutBottomSheet = SignOutDialog.create {
                signOut()
            }
            signOutBottomSheet.show(childFragmentManager, signOutBottomSheet.tag)
        }

        binding.toolbar.menu.findItem(R.id.call).setOnMenuItemClickListener {
             navigationScreen.goToCall(BuildConfig.SUPPORT_NUMBER)
            true
        }
    }

    private fun updateList() {
        LocalBroadcastManager.getInstance(requireContext()).also {
            it.registerReceiver(object : BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {
                    viewModel.callApi()
                }
            }, IntentFilter(MealPassFirebaseMessagingService.MERCHANT_BROADCAST))
        }
    }

    private fun signOut() {
        bindNetworkState(viewModel.logoutMethod(), progressDialog(R.string.Logout), onError = {
            viewModel.clearData()
            NotificationManagerCompat.from(requireContext()).cancelAll()
            navigationScreen.goToMainScreen(requireActivity() as MerchantActivity)
        }) {
            viewModel.clearData()
            NotificationManagerCompat.from(requireContext()).cancelAll()
            navigationScreen.goToMainScreen(requireActivity() as MerchantActivity)
        }
    }




//    private fun backStackResponseData() {
//        backStackGetIntData(IS_SHOP_OPEN)?.observe(viewLifecycleOwner,  {
//            it?.let { viewModel.merchantNotificationResponse?.is_open = it }
//        })
//        backStackGetData(BACK_STACK_DESCRIPTION)?.observe(viewLifecycleOwner,  {
//            it?.let {  viewModel.description = it }
//        })
//        backStackGetFloatData(BACK_STACK_DOUBLE)?.observe(viewLifecycleOwner,  {
//            it?.let { viewModel.merchantNotificationResponse?.cost_price = it }
//        })
//        backStackGetFloatData(RETAIL_PRICE)?.observe(viewLifecycleOwner,  {
//            it?.let { viewModel.merchantNotificationResponse?.retail_price = it }
//        })
//        backStackGetIntData(BACK_STACK_INT)?.observe(viewLifecycleOwner,  {
//            Log.e("portion",""+it)
//        })
//    }



    override fun onBindView(binding: FragmentMerchantNotificationBinding) {
        binding.vm = viewModel
    }
}
