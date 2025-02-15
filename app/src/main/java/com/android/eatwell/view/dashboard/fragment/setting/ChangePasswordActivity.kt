package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityChangePasswordBinding

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
