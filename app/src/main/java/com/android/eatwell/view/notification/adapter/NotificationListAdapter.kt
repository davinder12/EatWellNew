package com.android.eatwell.view.notification.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.models.NotificationResponse
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemNotificationBinding

class NotificationListAdapter : DataBoundAdapterClass<NotificationResponse.Body, ItemNotificationBinding>(diffCallback) {

    override val defaultLayoutRes: Int
        get() = R.layout.item_notification


    override fun bind(
            bind: ItemNotificationBinding,
            itemType: NotificationResponse.Body?,
            position: Int) {

        itemType?.let { item->
            bind.item = item
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NotificationResponse.Body>() {
            override fun areItemsTheSame(
                    oldItem: NotificationResponse.Body,
                    newItem: NotificationResponse.Body
            ): Boolean =
                oldItem == newItem


            override fun areContentsTheSame(
                    oldItem: NotificationResponse.Body,
                    newItem: NotificationResponse.Body
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun map(binding: ItemNotificationBinding): NotificationResponse.Body? {
        return binding.item
    }



}
