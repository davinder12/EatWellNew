package com.android.mealpass.view.login.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.login.instagramlogin.InstagramApp
import com.android.mealpass.view.login.viewmodel.StartUpFragmentViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentStartupBinding
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class StartUpFragment : BaseFragment<FragmentStartupBinding>() {


    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_startup

    var intagramApp :InstagramApp? = null




    companion object {
        const val FACEBOOK_FIELDS_KEY = "fields"
        const val FACEBOOK_FIELDS_VALUE = "id,name,email,gender,birthday"
        const val NAME = "name"
        const val EMAIL = "email"
        const val FACEBOOK_ID = "id"
        const val SOCIAL_TYPE = 0
        const val CALL_BACK ="https://www.mealpassapp.org/"
    }

    lateinit var callbackManager: CallbackManager

    private val viewModel: StartUpFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        binding.facebookLogin.fragment = this;
        binding.facebookLogin.setPermissions(listOf("public_profile", "email"))
        facebookLoginMethod()


        subscribe(binding.loginBtn.throttleClicks()) {
            findNavController().navigate(StartUpFragmentDirections.actionStartUpFragmentToLoginFragment())
        }
        subscribe(binding.signupBtn.throttleClicks()) {
            findNavController().navigate(StartUpFragmentDirections.actionStartUpFragmentToSignupFragment())
        }
        subscribe(binding.faceLoginLayout.throttleClicks()) {
            binding.facebookLogin.performClick();
        }

        subscribe(binding.businessLogin.throttleClicks()){
            val navigator = StartUpFragmentDirections.actionStartUpFragmentToLoginFragment().also {
                it.isMerchantLogin = true
            }
            findNavController().navigate(navigator)
        }

        subscribe(binding.instagramlogin.throttleClicks()){
            instagramIntial()
        }
    }

    private fun instagramIntial() {
        intagramApp = InstagramApp(
            requireContext(),
            BuildConfig.INSTAGRAM_CLIENT_ID,
            BuildConfig.INSTAGRAM_CLIENT_SECRET,
            CALL_BACK
        )
        intagramApp?.setListener(object : InstagramApp.OAuthAuthenticationListener {
            override fun onSuccess() {
                intagramApp?.fetchUserName(handler)
            }

            override fun onFail(error: Int) {
                showMessage(resources.getString(error))
            }
        })
        intagramApp?.authorize()
    }


    private var userInfoHashmap:  HashMap<String, String>?  = HashMap<String, String>()
    private val handler = Handler { msg ->
        if (msg.what == InstagramApp.WHAT_FINALIZE) {
            userInfoHashmap = intagramApp?.userInfo
            viewModel.name = userInfoHashmap?.get("username") ?:""
            viewModel.facebookId = userInfoHashmap?.get("id") ?:""
            bindNetworkState(
                viewModel.socialLoginMethod(),
                progressDialog(R.string.Pleasewait),
                R.string.login_success
            ) {
                navigationScreen.dashBoardScreenNavigation()
                requireActivity().finish()
            }
        } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
            showMessage(resources.getString(R.string.FailureMessage))
        }
        false
    }

    override fun onBindView(binding: FragmentStartupBinding) {
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
                                        navigationScreen.dashBoardScreenNavigation()
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
                parameters.putString(FACEBOOK_FIELDS_KEY, FACEBOOK_FIELDS_VALUE)
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
