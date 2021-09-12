package com.android.mealpass.view.dashboard.fragment.setting

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityChangePasswordBinding

@AndroidEntryPoint
class ChangePasswordActivity : DataBindingActivity<ActivityChangePasswordBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_change_password

    val viewModel : ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         subscribe(binding.changepassword.throttleClicks()){
             viewModel.filterMethod { status, message ->
                 when {
                     status -> bindNetworkState(
                             viewModel.changePasswordApi(),
                             progressDialog(R.string.ChangePassword),
                             R.string.password_updated_successfully
                     ) {
                         finish()
                     }
                     else -> showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                 }
             }
         }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onBindView(binding: ActivityChangePasswordBinding) {
        binding.vm  = viewModel
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }
}
