package com.android.mealpass.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ItemUsedReceiptBinding

class UsedReceiptAdapter : DataBoundAdapterClass<ReceiptResponse.Body.UsedReceipt, ItemUsedReceiptBinding>(Diff) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_used_receipt


    override fun bind(
        bind: ItemUsedReceiptBinding,
        itemType: ReceiptResponse.Body.UsedReceipt?,
        position: Int
    ) {
        bind.item = itemType
    }


    object Diff : DiffUtil.ItemCallback<ReceiptResponse.Body.UsedReceipt>() {
        override fun areItemsTheSame(
            oldItem: ReceiptResponse.Body.UsedReceipt,
            newItem: ReceiptResponse.Body.UsedReceipt
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ReceiptResponse.Body.UsedReceipt,
            newItem: ReceiptResponse.Body.UsedReceipt
        ): Boolean = oldItem == newItem
    }

    override fun map(binding: ItemUsedReceiptBinding): ReceiptResponse.Body.UsedReceipt? {
        return binding.item
    }
}
