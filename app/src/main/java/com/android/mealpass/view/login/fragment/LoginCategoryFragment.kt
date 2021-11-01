package com.android.mealpass.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentLoginOptionBinding


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
