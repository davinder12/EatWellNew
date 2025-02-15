package com.android.eatwell.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentLoginOptionBinding


@AndroidEntryPoint
class LoginCategoryFragment : BaseFragment<FragmentLoginOptionBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_login_option


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.personalAccountBtn.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(binding.businessAccountBtn.throttleClicks()) {
            findNavController().navigate(LoginCategoryFragmentDirections.actionLoginCategoryFragmentToMerchantLoginCategoryFragment())
        }

        subscribe(binding.personalAccountBtn.throttleClicks()) {
            val navigator = LoginCategoryFragmentDirections.actionLoginCategoryFragmentToStartUpFragment()
            findNavController().navigate(navigator)
        }

    }
}
