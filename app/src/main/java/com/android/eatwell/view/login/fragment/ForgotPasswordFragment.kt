package com.android.eatwell.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseFragment
import com.android.eatwell.view.login.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentForgotpasswordBinding


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
