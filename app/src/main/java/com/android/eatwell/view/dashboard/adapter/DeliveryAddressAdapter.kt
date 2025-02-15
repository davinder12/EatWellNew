package com.android.eatwell.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.extension.throttle
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemDeliveryAddressBinding

class DeliveryAddressAdapter : DataBoundAdapterClass<String, ItemDeliveryAddressBinding>(diffCallback) {
    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_delivery_address


    private var closeClickEmitter: ObservableEmitter<Int>? = null
    val closeBtn: Observable<Int> =
            Observable.create<Int> { closeClickEmitter = it }.throttle()


    override fun bind(
        bind: ItemDeliveryAddressBinding,
        itemType: String?,
        position: Int
    ) {
        itemType?.let {  item ->
            bind.item = item
            bind.closeBtn.throttleClicks().subscribe{
                closeClickEmitter?.onNext(position)
            }
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
