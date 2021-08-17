package com.android.mealpass.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import com.android.mealpass.view.login.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentForgotpasswordBinding


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotpasswordBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_forgotpassword

    private val viewModel: ForgotPasswordViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(binding.send.throttleClicks()) {
            viewModel.filterMethod { condition, message ->
                when {
                    condition -> {
                        bindNetworkState(
                            viewModel.callForgotPwdApi(),
                            progressDialog(R.string.Pleasewait),
                            R.string.forgotpwd_success
                        ) {
                            binding.cross.performClick()
                        }
                    }
                    else -> showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                }
            }
        }
    }

    override fun onBindView(binding: FragmentForgotpasswordBinding) {
        binding.vm = viewModel
    }

}
