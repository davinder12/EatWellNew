package com.android.mealpass.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.login.viewmodel.SignupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signup.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentSignupBinding
import javax.inject.Inject


@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private val viewModel: SignupFragmentViewModel by viewModels()

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_signup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(cross.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(terms_condition.throttleClicks()) {
            navigationScreen.goToTermAndConditionScreen(false)
        }

        subscribe(binding.signup.throttleClicks()) {
            viewModel.filterMethod { status, message ->
                when {
                    status -> {
                        bindNetworkState(
                                viewModel.callSignUpApi(checkbox.isChecked),
                                progressDialog(R.string.Pleasewait),
                                R.string.success_register
                        ) {
                            navigationScreen.goToChooseReferralScreen()
                            requireActivity().finish()
                        }
                    }
                    else -> {
                        showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                    }
                }
            }
        }
    }

    override fun onBindView(binding: FragmentSignupBinding) {
        binding.vm = viewModel
    }


}
