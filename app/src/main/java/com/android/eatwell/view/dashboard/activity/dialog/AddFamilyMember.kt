package com.android.eatwell.view.dashboard.activity.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseDialogFragment
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.ReferralCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.DialogAddFamilyMemberBinding

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