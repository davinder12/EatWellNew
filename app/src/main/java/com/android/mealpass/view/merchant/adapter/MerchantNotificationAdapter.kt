package com.android.mealpass.view.merchant.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.mealpass.data.extension.throttle
import com.android.mealpass.data.models.MerchantNotificationResponse
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ItemMerchantNotificationBinding

class MerchantNotificationAdapter() :
    DataBoundAdapterClass<MerchantNotificationResponse.Body.Notifiction, ItemMerchantNotificationBinding>(
        diffCallback
    ) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_merchant_notification

    private var contentClickEmitter: ObservableEmitter<MerchantNotificationResponse.Body.Notifiction>? = null
    val contentClicks: Observable<MerchantNotificationResponse.Body.Notifiction> =
        Observable.create<MerchantNotificationResponse.Body.Notifiction> {
            contentClickEmitter = it
        }.throttle()


    override fun bind(
        bind: ItemMerchantNotificationBinding,
        itemType: MerchantNotificationResponse.Body.Notifiction?,
        position: Int
    ) {
        itemType?.let { response ->
            bind.item = response
        }
    }

    override fun map(binding: ItemMerchantNotificationBinding): MerchantNotificationResponse.Body.Notifiction? {
        return binding.item
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<MerchantNotificationResponse.Body.Notifiction>() {
                override fun areItemsTheSame(
                    oldItem: MerchantNotificationResponse.Body.Notifiction,
                    newItem: MerchantNotificationResponse.Body.Notifiction
                ): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: MerchantNotificationResponse.Body.Notifiction,
                    newItem: MerchantNotificationResponse.Body.Notifiction
                ): Boolean = oldItem == newItem
            }
    }
}
