package com.android.mealpass.view.merchant.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.merchant.viewmodel.MerchantPortionModel
import com.android.mealpass.widgets.alertDialog
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentMerchantPortionBinding


@AndroidEntryPoint
class MerchantPortion : BaseListFragment<FragmentMerchantPortionBinding>() {


    companion object {
        const val SHOP_OPEN = 1
        const val SHOP_CLOSE = 0
        const val PORTION_LIMIT = 0
        const val RETAIL_PRICE =  "RETAIL_PRICE"
        const  val IS_SHOP_OPEN = "IS_SHOP_OPEN"
    }

    private val viewModel: MerchantPortionModel by viewModels()
//
    private val args: MerchantPortionArgs by navArgs()

    override val layoutRes: Int
        get() = R.layout.fragment_merchant_portion


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateData(args.productDetail)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.plusCount.setOnClickListener {
           viewModel.incrementUpdate()
        }

        binding.minusCount.setOnClickListener {
           viewModel.decrementUpdate()
        }
       

        subscribe(binding.switcher.throttleClicks()){
           val message : String =   if (viewModel.isOpen.value == SHOP_OPEN) resources.getString(R.string.CloseShopAlert) else resources.getString(R.string.OpenShopAlert)
            requireContext().alertDialog(resources.getString(R.string.app_name),
                    message,
                    getString(R.string.Continue),
                    getString(R.string.Cancel),
                    successResponse = {
                        bindNetworkState(viewModel.updateResturantStatus(), progressDialog(R.string.Pleasewait)) {
                            backStackPutInt(IS_SHOP_OPEN, viewModel.isOpen.value)
                        }
                    })

        }

        subscribe(binding.savePortion.throttleClicks()) {
            if (viewModel.remainingPortion.value?: 0 > 0) {
                bindNetworkState(viewModel.updateMerchantPortion(), progressDialog(R.string.Pleasewait)) {
                    backStackPutInt(BACK_STACK_INT, viewModel.remainingPortion.value)
                    bindNetworkState(viewModel.updatePortionNotificationMethod, progressDialog(R.string.Pleasewait))
                }
            }
        }

        subscribe(binding.retailPriceBtn.throttleClicks()){
            requireContext().alertDialog(resources.getString(R.string.app_name),
                    resources.getString(R.string.UpdateRetailAlert),
                    getString(R.string.Continue),
                    getString(R.string.Cancel),
                    successResponse = {
                        val apiProgress = viewModel.updateRetailPriceOrCostPrice(viewModel.retailPrice.value, "")
                        bindNetworkState(apiProgress, progressDialog(R.string.Pleasewait)) {
                            backStackPutFloat(RETAIL_PRICE, viewModel.retailPrice.value?.toFloatOrNull())
                        }
                    })

        }

        subscribe(binding.updatePrice.throttleClicks()) {
            requireContext().alertDialog(resources.getString(R.string.app_name),
                    resources.getString(R.string.UpdatePriceAlert),
                    getString(R.string.Continue),
                    getString(R.string.Cancel),
                    successResponse = {
                        val apiProgress = viewModel.updateRetailPriceOrCostPrice("", viewModel.costPrice.value)
                        bindNetworkState(apiProgress, progressDialog(R.string.Pleasewait)) {
                            backStackPutFloat(BACK_STACK_DOUBLE, viewModel.costPrice.value?.toFloatOrNull())
                        }
                    })
        }
    }


    override fun onBindView(binding: FragmentMerchantPortionBinding) {
        binding.vm = viewModel
    }


}
