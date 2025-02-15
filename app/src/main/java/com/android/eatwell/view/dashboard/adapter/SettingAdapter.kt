package com.android.eatwell.view.dashboard.adapter

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.android.eatwell.data.api.enums.SettingEnum
import com.android.eatwell.data.extension.throttle
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseadapter.DataBoundAdapterClass
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ItemSettingBinding

class SettingAdapter(private val locationNotification: MutableLiveData<Boolean>, private val versionName: String) : DataBoundAdapterClass<SettingEnum, ItemSettingBinding>(diffCallback) {
    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_setting

    private var itemClickEmitter: ObservableEmitter<SettingEnum>? = null
    val itemClicks: Observable<SettingEnum> = Observable.create<SettingEnum> {
        itemClickEmitter = it
    }.throttle()


    override fun bind(
        bind: ItemSettingBinding,
        itemType: SettingEnum?,
        position: Int
    ) {

        itemType?.let { item->
            bind.item = item
            bind.isNotificationOn = locationNotification
            bind.version = versionName
            bind.toogleBtn.throttleClicks().subscribe {
                itemClickEmitter?.onNext(item)

            }
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SettingEnum>() {
            override fun areItemsTheSame(
                oldItem: SettingEnum,
                newItem: SettingEnum
            ): Boolean =
                oldItem == newItem

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(
                oldItem: SettingEnum,
                newItem: SettingEnum
            ): Boolean =
                oldItem == newItem
        }
    }


    override fun map(binding: ItemSettingBinding): SettingEnum? {
        return binding.item
    }



}
