package com.android.eatwell.view.merchant.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.merchant.viewmodel.MerchantPortionModel
import com.android.eatwell.view.units.floatTwoDigits
import com.android.eatwell.widgets.alertDialog
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentMerchantPortionBinding


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

    private val args: MerchantPortionArgs by navArgs()

    override val layoutRes: Int
        get() = R.layout.fragment_merchant_portion


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateData(args.productDetail)

        viewModel.updatedCondition.observe(viewLifecycleOwner) {
            viewModel.monthlyTaxDeduction.value = it.floatTwoDigits()
            viewModel.yearlyTaxDeduction.value = (it * 365).floatTwoDigits()
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.plusCount.setOnClickListener {
            viewModel.incrementUpdate()
        }

        binding.minusCount.setOnClickListener {
            viewModel.decrementUpdate {
                if (it) {
                    showSnackMessage(getString(R.string.SoldPortionAlertMsg))
                }
            }
        }


        subscribe(binding.switcher.throttleClicks()){
           val message : String =   if (viewModel.isOpen.value == SHOP_OPEN) resources.getString(R.string.CloseShopAlert) else resources.getString(R.string.OpenShopAlert)
            requireContext().alertDialog(resources.getString(R.string.app_name),
                    message,
                    getString(R.string.Continue),
                    getString(R.string.Cancel),
                    successResponse = {
                        bindNetworkState(viewModel.updateResturantStatus(), progressDialog(R.string.Pleasewait)) {
                            //backStackPutInt(IS_SHOP_OPEN, viewModel.isOpen.value)
                        }
                    })

        }

        subscribe(binding.savePortion.throttleClicks()) {
            viewModel.filterMethod { condition ->
                if (condition) {
                    bindNetworkState(viewModel.callProductDetailApi(), progressDialog(R.string.Pleasewait), R.string.PortionUpdated) {
                        backStackPutInt(BACK_STACK_INT, viewModel.totalPortion.value)
                        bindNetworkState(viewModel.updatePortionNotificationMethod, progressDialog(R.string.UpdateToServer))
                    }
                }
            }
        }


        subscribe(binding.retailPriceBtn.throttleClicks()) {
            viewModel.retailPrice.value?.toFloatOrNull()?.let { retailPrice ->
                requireContext().alertDialog(resources.getString(R.string.app_name),
                        resources.getString(R.string.UpdateRetailAlert),
                        getString(R.string.Continue),
                        getString(R.string.Cancel),
                        successResponse = {
                            val apiProgress = viewModel.updateRetailPriceOrCostPrice(retailPrice.toString(), "")
                            bindNetworkState(apiProgress, progressDialog(R.string.Pleasewait), R.string.PriceUpdated) {
                            }
                        })
            }
        }

        subscribe(binding.updatePrice.throttleClicks()) {
            viewModel.costPrice.value?.toFloatOrNull()?.let { costPrice ->
                requireContext().alertDialog(resources.getString(R.string.app_name),
                        resources.getString(R.string.UpdatePriceAlert),
                        getString(R.string.Continue),
                        getString(R.string.Cancel),
                        successResponse = {
                            val apiProgress = viewModel.updateRetailPriceOrCostPrice("", costPrice.toString())
                            bindNetworkState(apiProgress, progressDialog(R.string.Pleasewait),R.string.PriceUpdated) {
                             //   backStackPutFloat(BACK_STACK_DOUBLE, viewModel.costPrice.value?.toFloatOrNull())
                            }
                        })
            }
        }
    }


    override fun onBindView(binding: FragmentMerchantPortionBinding) {
        binding.vm = viewModel

    }

}
