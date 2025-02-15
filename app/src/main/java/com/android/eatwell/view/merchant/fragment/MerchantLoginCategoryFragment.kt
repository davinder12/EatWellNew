package com.android.eatwell.view.merchant.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseFragment
import com.android.eatwell.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentMerchantCategoryBinding
import javax.inject.Inject


@AndroidEntryPoint
class MerchantLoginCategoryFragment : BaseFragment<FragmentMerchantCategoryBinding>() {

    companion object {
        const val MERCHANT_URL = "https://www.mealpassapp.org/contact-8?fbclid=IwAR21UpapQmAdPPJxyUjuBzUTVVpLnPucfnI0jk5QoBgB-op9NcOLwkRhxTU"
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
