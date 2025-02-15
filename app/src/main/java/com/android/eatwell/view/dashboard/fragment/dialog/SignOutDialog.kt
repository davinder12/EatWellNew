package com.android.eatwell.view.dashboard.fragment.dialog

import android.os.Bundle
import android.view.View
import com.android.eatwell.utilitiesclasses.baseclass.BaseBottomSheetDialogFragment
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.SignOutBottomSheetBinding


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
