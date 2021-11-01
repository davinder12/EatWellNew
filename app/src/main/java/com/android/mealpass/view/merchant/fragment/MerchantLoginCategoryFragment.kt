package com.android.mealpass.view.merchant.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import com.android.mealpass.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentMerchantCategoryBinding
import javax.inject.Inject


@AndroidEntryPoint
class MerchantLoginCategoryFragment : BaseFragment<FragmentMerchantCategoryBinding>() {

    companion object {
        const val MERCHANT_URL = "https://mealpass.org/admin/merchant/index.php"
    }

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_merchant_category


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.merchantRegisterBtn.throttleClicks()) {
            navigationScreen.goToSocialPage(MERCHANT_URL)
        }

        subscribe(binding.merchantLoginBtn.throttleClicks()) {
            val navigator = MerchantLoginCategoryFragmentDirections.actionMerchantLoginCategoryFragmentToLoginFragment().also {
                it.isMerchantLogin = true
            }
            findNavController().navigate(navigator)
        }

        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }

    }
}
