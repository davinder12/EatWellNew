package com.android.eatwell.view.login.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.login.viewmodel.SignupFragmentViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentSignupBinding
import javax.inject.Inject


@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private val viewModel: SignupFragmentViewModel by viewModels()

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_signup

    lateinit var callbackManager: CallbackManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        binding.facebookSignUpBtn.fragment = this
        binding.facebookSignUpBtn.setPermissions(listOf("public_profile", "email"))
        facebookLoginMethod()

        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(binding.registerWithFacebook.throttleClicks()) {
            binding.facebookSignUpBtn.performClick()
        }
        subscribe(binding.termsCondition.throttleClicks()) {
            navigationScreen.goToTermAndConditionScreen(false)
        }

        subscribe(binding.signup.throttleClicks()) {
            viewModel.filterMethod { status, message ->
                when {
                    status -> {
                        bindNetworkState(
                                viewModel.callSignUpApi(binding.checkbox.isChecked),
                                progressDialog(R.string.Pleasewait),
                                R.string.success_register
                        ) {
                            //navigationScreen.goToChooseReferralScreen()
                            //requireActivity().finish()
                            navigationScreen.goToReferralCodeScreen(true)
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

    private fun facebookLoginMethod() {
        LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val request =
                        GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, response ->
                            try {
                                viewModel.getResponseFromFacebook(jsonObject) { isCondition, message ->
                                    if (isCondition) {
                                        bindNetworkState(viewModel.socialLoginMethod(), progressDialog(R.string.Pleasewait), R.string.login_success) {
                                            navigationScreen.goToReferralCodeScreen(true)
                                            requireActivity().finish()
                                        }
                                    } else {
                                        showSnackMessage(resources.getString(message))
                                    }
                                }
                                LoginManager.getInstance().logOut()
                            } catch (e: Exception) {
                                showSnackMessage(e.message)
                                LoginManager.getInstance().logOut()
                            }
                        }
                val parameters = Bundle()
                parameters.putString(StartUpFragment.FACEBOOK_FIELDS_KEY, StartUpFragment.FACEBOOK_FIELDS_VALUE)
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                showSnackMessage(resources.getString(R.string.FacebookConnection))
            }

            override fun onError(error: FacebookException) {
                showSnackMessage(error.message)
            }
        })
    }

    override fun onBindView(binding: FragmentSignupBinding) {
        binding.vm = viewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
