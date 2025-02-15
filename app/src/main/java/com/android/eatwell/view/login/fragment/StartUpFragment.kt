package com.android.eatwell.view.login.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.login.instagramlogin.InstagramApp
import com.android.eatwell.view.login.viewmodel.StartUpFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentStartupBinding
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



    private val viewModel: StartUpFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        subscribe(binding.loginBtn.throttleClicks()) {
            findNavController().navigate(StartUpFragmentDirections.actionStartUpFragmentToLoginFragment())
        }
        subscribe(binding.signupBtn.throttleClicks()) {
            findNavController().navigate(StartUpFragmentDirections.actionStartUpFragmentToSignupFragment())
        }

        subscribe(binding.cross.throttleClicks()) {
            findNavController().popBackStack()
        }
//        subscribe(binding.faceLoginLayout.throttleClicks()) {
//            binding.facebookLogin.performClick();
//        }

        subscribe(binding.businessLogin.throttleClicks()) {
            val navigator = StartUpFragmentDirections.actionStartUpFragmentToLoginFragment().also {
                it.isMerchantLogin = true
            }
            findNavController().navigate(navigator)
        }

//        subscribe(binding.instagramlogin.throttleClicks()){
//            instagramIntial()
//        }
    }


    override fun onBindView(binding: FragmentStartupBinding) {
        binding.vm = viewModel
    }





}
