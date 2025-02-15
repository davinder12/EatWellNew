package com.android.eatwell.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.models.ProductTypeResponse
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemCategoryLayoutBinding

class FoodFilterAdapter :
    DataBoundAdapterClass<ProductTypeResponse.Body, ItemCategoryLayoutBinding>(diffCallback) {

    override val defaultLayoutRes: Int
        get() = R.layout.item_category_layout


    override fun bind(
        bind: ItemCategoryLayoutBinding,
        itemType: ProductTypeResponse.Body?,
        position: Int
    ) {
        itemType?.let {
            bind.vm = it
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ProductTypeResponse.Body>() {
            override fun areItemsTheSame(
                oldItem: ProductTypeResponse.Body,
                newItem: ProductTypeResponse.Body
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProductTypeResponse.Body,
                newItem: ProductTypeResponse.Body
            ): Boolean = (oldItem.id == newItem.id)
        }
    }


    override fun map(binding: ItemCategoryLayoutBinding): ProductTypeResponse.Body? {
        return binding.vm
    }


}
