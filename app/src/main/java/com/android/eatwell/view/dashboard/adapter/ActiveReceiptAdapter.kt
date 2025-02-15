package com.android.eatwell.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.models.ReceiptResponse
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemReceiptBinding

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
