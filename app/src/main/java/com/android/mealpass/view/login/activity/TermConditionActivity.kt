package com.android.mealpass.view.login.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.login.viewmodel.TermAndConditionViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityTermConditionBinding


@AndroidEntryPoint
class TermConditionActivity : DataBindingActivity<ActivityTermConditionBinding>() {

    companion object{
        const val MERCHANT_LOGIN = "1"
        const val CUSTOMER_LOGIN = "0"

    }

    private val viewModel: TermAndConditionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status = if(intent.getBooleanExtra(NavigationScreen.MERCHANT_LOGIN, false)) MERCHANT_LOGIN else CUSTOMER_LOGIN
        bindNetworkState(viewModel.callTermConditionMethod(status),loadingIndicator = binding.termConditionProgress)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override val layoutRes: Int
        get() = R.layout.activity_term_condition

    override fun onBindView(binding: ActivityTermConditionBinding) {
        binding.vm = viewModel
    }

}
