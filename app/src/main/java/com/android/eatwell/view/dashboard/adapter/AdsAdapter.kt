package com.android.eatwell.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.models.AdsResponse
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemAdsBinding

class AdsAdapter : DataBoundAdapterClass<AdsResponse.Body, ItemAdsBinding>(diffCallback) {

    override val defaultLayoutRes: Int
        get() = R.layout.item_ads


    override fun bind(
            bind: ItemAdsBinding,
            itemType: AdsResponse.Body?,
            position: Int) {

        itemType?.let { item->
            bind.item = item
            when{
                item.description.isNullOrEmpty() ->bind.description = item.description
                else -> bind.pictureUrl =  item.picture_url
            }
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<AdsResponse.Body>() {
            override fun areItemsTheSame(
                    oldItem: AdsResponse.Body,
                    newItem: AdsResponse.Body
            ): Boolean =
                oldItem == newItem

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(
                    oldItem: AdsResponse.Body,
                    newItem: AdsResponse.Body
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun map(binding: ItemAdsBinding): AdsResponse.Body? {
        return binding.item
    }



}
