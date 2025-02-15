package com.android.eatwell.view.notification

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.service.MealPassFirebaseMessagingService
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.notification.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.GeneralNotificationBinding

@AndroidEntryPoint
class GeneralNotificationActivity : DataBindingActivity<GeneralNotificationBinding>() {


    private val viewModel: NotificationViewModel by viewModels()


    override val layoutRes: Int
        get() = R.layout.general_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateBadgesCount()

        intent.extras?.getString(MealPassFirebaseMessagingService.MESSAGE, "")?.let {
          binding.message = it
        }
        intent.extras?.getString(MealPassFirebaseMessagingService.MERCHANT_LOGO, "")?.let {
            binding.merchantlogo = it
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

}