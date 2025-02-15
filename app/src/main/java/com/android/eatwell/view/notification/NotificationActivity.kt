package com.android.eatwell.view.notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.notification.adapter.NotificationListAdapter
import com.android.eatwell.view.notification.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityNotificationBinding
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActivity : DataBindingActivity<ActivityNotificationBinding>() {

    companion object {
      const val GENENRAL_UPDATE_TYPE = 3
    }

    @Inject
    lateinit var navigationScreen: NavigationScreen

    private val viewModel: NotificationViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateBadges()
        NotificationManagerCompat.from(this).cancelAll()

        initAdapter(NotificationListAdapter(), binding.notificationlist, viewModel.notificationList, viewModel.resource) {
            when {
                it.notification_type == GENENRAL_UPDATE_TYPE -> navigationScreen.goToGeneralNotification(it.message, it.merchant_logo)
                else -> navigationScreen.productDetailScreen(it.merchant_id, it.merchant_name)
            }
        }

        subscribe(emptyView.throttleClicks()) {
            viewModel.callApi()
        }

        binding.toolbar.setNavigationOnClickListener { finish() }
    }



}