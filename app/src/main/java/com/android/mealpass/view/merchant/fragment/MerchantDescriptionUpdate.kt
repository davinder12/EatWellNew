package com.android.mealpass.view.merchant.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.merchant.viewmodel.MerchantDescriptionUpdateModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentMerchantDescriptionUpdateBinding

@AndroidEntryPoint
class MerchantDescriptionUpdate : BaseListFragment<FragmentMerchantDescriptionUpdateBinding>() {

    private val args: MerchantDescriptionUpdateArgs by navArgs()

    override val layoutRes: Int
        get() = R.layout.fragment_merchant_description_update

    private val viewModel: MerchantDescriptionUpdateModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.updateData(args.description)

        subscribe(binding.updateDescription.throttleClicks()) {
            viewModel.description.value?.let {
                if (it.isNotEmpty()) {
                    bindNetworkState(
                        viewModel.updateMerchantDescription(),
                        progressDialog(R.string.Pleasewait),R.string.DescUpdated) {
                        backStackPutData(BACK_STACK_DESCRIPTION, it)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onBindView(binding: FragmentMerchantDescriptionUpdateBinding) {
        binding.vm = viewModel
    }

}
