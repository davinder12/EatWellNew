package com.android.eatwell.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.models.FoodData
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemFoodBinding

class FavouriteFoodAdapter : DataBoundAdapterClass<FoodData.Body, ItemFoodBinding>(diffCallback) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_food


    override fun bind(
        bind: ItemFoodBinding,
        itemType: FoodData.Body?,
        position: Int
    ) {
        itemType?.let { response ->
            bind.vm = response

        }
    }

    override fun map(binding: ItemFoodBinding): FoodData.Body? {
        return binding.vm
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<FoodData.Body>() {
            override fun areItemsTheSame(
                oldItem: FoodData.Body,
                newItem: FoodData.Body
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FoodData.Body,
                newItem: FoodData.Body
            ): Boolean = oldItem.id == newItem.id
        }
    }
}
