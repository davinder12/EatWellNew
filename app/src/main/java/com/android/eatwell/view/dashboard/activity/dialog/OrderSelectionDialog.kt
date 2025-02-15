package com.android.eatwell.view.dashboard.activity.dialog

import android.os.Bundle
import android.view.View
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.DialogOrderSelectionBinding

@AndroidEntryPoint
class OrderSelectionDialog : BaseDialogFragment<DialogOrderSelectionBinding>() {

    override val layoutRes: Int
        get() = R.layout.dialog_order_selection


    companion object {
        var itemLeft: Int = 0
        var defaultPortion: Int = 1
        lateinit var callBack: (Int) -> Unit

        fun create(itemLeft: Int, callBackMethod: (Int) -> Unit): OrderSelectionDialog {
            this.itemLeft = itemLeft
            defaultPortion = 1
            callBack = callBackMethod
            return OrderSelectionDialog()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.buy.throttleClicks()) {
            callBack.invoke(defaultPortion)
            dismiss()
        }

        binding.plusCount.setOnClickListener {
            if (itemLeft > defaultPortion) {
                defaultPortion = defaultPortion.inc()
                binding.quantity.text = defaultPortion.toString()
            }
        }

        binding.minusCount.setOnClickListener {
            if (defaultPortion > 1) {
                defaultPortion = defaultPortion.dec()
                binding.quantity.text = defaultPortion.toString()
            }
        }
    }
//    override fun onBindView(binding: DialogOrderSelectionBinding) {
//        binding.vm = viewModel
//    }
}