package com.android.mealpass.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ItemReceiptBinding

class ActiveReceiptAdapter : DataBoundAdapterClass<ReceiptResponse.Body.ActiveReceipt, ItemReceiptBinding>(Diff) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_receipt


    override fun bind(
        bind: ItemReceiptBinding,
        itemType: ReceiptResponse.Body.ActiveReceipt?,
        position: Int
    ) {
        bind.item = itemType
    }


    object Diff : DiffUtil.ItemCallback<ReceiptResponse.Body.ActiveReceipt>() {
        override fun areItemsTheSame(
            oldItem: ReceiptResponse.Body.ActiveReceipt,
            newItem: ReceiptResponse.Body.ActiveReceipt
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ReceiptResponse.Body.ActiveReceipt,
            newItem: ReceiptResponse.Body.ActiveReceipt
        ): Boolean = oldItem == newItem
    }

    override fun map(binding: ItemReceiptBinding): ReceiptResponse.Body.ActiveReceipt? {
        return binding.item
    }
}
