package com.android.mealpass.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.login.viewmodel.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentLoginBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_login

    private val viewModel: LoginFragmentViewModel by viewModels()

    val  argument : LoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(binding.forgotPwd.throttleClicks()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        subscribe(binding.login.throttleClicks()) {
            viewModel.filterMethod { isCondition, message ->
                when {
                    isCondition -> callApiMethod()
                    else -> {
                        showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                    }
                }
            }
        }
    }


    private fun callApiMethod(){
        if(argument.isMerchantLogin){
            bindNetworkState(viewModel.merchantSignInMethod(), progressDialog(R.string.Pleasewait), R.string.login_success) {
                navigationScreen.goToMerchantScreen()
                requireActivity().finish()
            }
        }else{
            bindNetworkState(viewModel.callSignInApi(), progressDialog(R.string.Pleasewait), R.string.login_success) {
                navigationScreen.dashBoardScreenNavigation()
                requireActivity().finish()
            }
        }
    }


    override fun onBindView(binding: FragmentLoginBinding) {
        binding.vm = viewModel
    }


}
