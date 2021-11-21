package com.android.mealpass.view.login.fragment

import android.content.Intent
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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

    private val argument: LoginFragmentArgs by navArgs()

    lateinit var callbackManager: CallbackManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        callbackManager = CallbackManager.Factory.create()
        binding.facebookLogin.fragment = this
        binding.facebookLogin.setPermissions(listOf("public_profile", "email"))
        facebookLoginMethod()
        viewModel.updateLoginType(argument.isMerchantLogin)
        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }

        subscribe(binding.forgotPwd.throttleClicks()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        subscribe(binding.termsCondition.throttleClicks()) {
            navigationScreen.goToTermAndConditionScreen(argument.isMerchantLogin)
        }

        subscribe(binding.faceBookLoginBtn.throttleClicks()) {
            binding.facebookLogin.performClick()
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
                navigationScreen.goToDashBoard()
                requireActivity().finish()
            }
        }
    }


    override fun onBindView(binding: FragmentLoginBinding) {
        binding.vm = viewModel
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
                                        bindNetworkState(
                                                viewModel.socialLoginMethod(), progressDialog(
                                                R.string.Pleasewait
                                        ), R.string.login_success
                                        ) {
                                            navigationScreen.goToDashBoard()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
