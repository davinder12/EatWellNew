package com.android.mealpass.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ItemDeliveryAddressBinding

class DeliveryAddressAdapter : DataBoundAdapterClass<String, ItemDeliveryAddressBinding>(diffCallback) {
    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_delivery_address


    override fun bind(
        bind: ItemDeliveryAddressBinding,
        itemType: String?,
        position: Int
    ) {
        itemType?.let {
            bind.item = it
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem
        }
    }

    override fun map(binding: ItemDeliveryAddressBinding): String? {
        return binding.item
    }
}
