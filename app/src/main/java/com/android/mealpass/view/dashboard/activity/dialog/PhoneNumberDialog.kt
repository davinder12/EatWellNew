package com.android.mealpass.view.dashboard.activity.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseDialogFragment
import com.android.mealpass.view.dashboard.viewmodel.PhoneUpdateViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.DialogPhoneUpdateBinding

@AndroidEntryPoint
class PhoneNumberDialog : BaseDialogFragment<DialogPhoneUpdateBinding>() {

    private val viewModel: PhoneUpdateViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.dialog_phone_update


    companion object {
        lateinit var callBack: () -> Unit
        fun create(callBackMethod: () -> Unit): PhoneNumberDialog {
            callBack = callBackMethod
            return PhoneNumberDialog()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(binding.update.throttleClicks()) {
            viewModel.filterMethod { condition, message ->
                if (condition) {
                    bindNetworkState(
                        viewModel.updatePhoneNumber(),
                        loadingIndicator = binding.progressBar2
                    ) {
                        callBack.invoke()
                        dismiss()
                    }
                }
            }
        }
        subscribe(binding.cancel.throttleClicks()) {
            dismiss()
        }
    }

    override fun onBindView(binding: DialogPhoneUpdateBinding) {
        binding.vm = viewModel
    }
}