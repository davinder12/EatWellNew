package com.android.mealpass.view.notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.android.mealpass.data.service.MealPassFirebaseMessagingService
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.viewmodel.FindFoodViewModel
import com.android.mealpass.view.notification.adapter.NotificationListAdapter
import com.android.mealpass.view.notification.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityNotificationBinding
import mealpass.com.mealpass.databinding.GeneralNotificationBinding
import javax.inject.Inject

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