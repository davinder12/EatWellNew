package com.android.eatwell.view.dashboard.fragment.dialog

import android.os.Bundle
import android.view.View
import com.android.eatwell.utilitiesclasses.baseclass.BaseBottomSheetDialogFragment
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.DeleteAccountBottomSheetBinding


class DeleteAccountDialog : BaseBottomSheetDialogFragment<DeleteAccountBottomSheetBinding>() {
    override val layoutRes = R.layout.delete_account_bottom_sheet

    companion object {
        var response: (() -> Unit)? = null
        fun create(response: (() -> Unit)): DeleteAccountDialog {
            this.response = response
            return DeleteAccountDialog()
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
