package com.android.mealpass.view.dashboard.activity.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.BaseDialogFragment
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.DialogAddFamilyMemberBinding

@AndroidEntryPoint
class AddFamilyMember : BaseDialogFragment<DialogAddFamilyMemberBinding>() {

    private val viewModel: ReferralCodeViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.dialog_add_family_member


    companion object {
        lateinit var callBack: () -> Unit
        fun create(callBackMethod: () -> Unit): AddFamilyMember {
            callBack = callBackMethod
            return AddFamilyMember()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        subscribe(binding.update.throttleClicks()) {
            viewModel.familyMemberfilterMethod { status, message ->
                if (status) {
                    bindNetworkState(viewModel.updateFamilyCount(), progressDialog(R.string.familyMemberUpdate)) {
                        hideKeboard()
                        dialog?.dismiss()
                        callBack.invoke()
                    }
                }
            }
        }

        subscribe(binding.cancel.throttleClicks()) {
            hideKeboard()
            dialog?.dismiss()
            callBack.invoke()
        }
    }


    private fun hideKeboard() {
        requireActivity().currentFocus?.let {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.code.windowToken, 0)
        }
    }

    override fun onBindView(binding: DialogAddFamilyMemberBinding) {
        binding.vm = viewModel
    }
}