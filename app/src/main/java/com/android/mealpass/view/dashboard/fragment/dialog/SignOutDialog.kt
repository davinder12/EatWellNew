package com.android.mealpass.view.dashboard.fragment.dialog

import android.os.Bundle
import android.view.View
import com.android.mealpass.utilitiesclasses.baseclass.BaseBottomSheetDialogFragment
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.SignOutBottomSheetBinding


class SignOutDialog : BaseBottomSheetDialogFragment<SignOutBottomSheetBinding>() {
    override val layoutRes = R.layout.sign_out_bottom_sheet

    companion object {
        var response: (() -> Unit)? = null
        fun create(response: (() -> Unit)): SignOutDialog {
            this.response = response
            return SignOutDialog()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signOutBtn.setOnClickListener {
            response?.invoke()
            dismiss()
        }
    }




}
