package com.android.mealpass.view.dashboard.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.viewmodel.CampaignViewModel
import com.android.mealpass.view.units.ReminderOrderNotification
import com.android.mealpass.view.units.getLocatDate
import com.android.mealpass.view.units.isLocalTimeGreaterThan60Min
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityCampaignCodeBinding
import org.threeten.bp.LocalTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class Campaign : DataBindingActivity<ActivityCampaignCodeBinding>() {

    override val layoutRes: Int get() = R.layout.activity_campaign_code
    private val viewModel: CampaignViewModel by viewModels()

    @Inject
    lateinit var navigationScreen: NavigationScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requirePaymentRequest()?.let {
            viewModel.storeName.value = it.storeName
        }

        subscribe(binding.getmagicPortion.throttleClicks()) {
            requirePaymentRequest()?.let {
                bindNetworkState(viewModel.updateReceiptRequest(it), progressDialog(R.string.Pleasewait)) {
                    viewModel.response?.let { data ->
                        getLocatDate(it.collection_from_time)?.let {
                            if (isLocalTimeGreaterThan60Min(it)) {
                                notificationMethod(it)
                            }
                            val intent = navigationScreen.goToActiveReceipt(data, true)
                            startActivity(intent)
                        }
                    }
                    finish()
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun notificationMethod(localTime: LocalTime) {
        val alarmManager = getSystemService(ALARM_SERVICE) as? AlarmManager
        val alarmType = AlarmManager.RTC_WAKEUP
        val pendingIntent: PendingIntent = Intent(this, ReminderOrderNotification::class.java).let { intent ->
//            val bundle = Bundle()
//            bundle.putParcelable(EXTRA_ACTIVE_RECEIPT_DETAIL,data)
//            intent.putExtra(EXTRA_ACTIVE_RECEIPT_DETAIL,bundle)
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, localTime.hour)
            set(Calendar.MINUTE, localTime.minute)
        }
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> alarmManager?.setExactAndAllowWhileIdle(alarmType, calendar.timeInMillis, pendingIntent)
            else -> alarmManager?.setExact(alarmType, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun onBindView(binding: ActivityCampaignCodeBinding) {
        binding.vm = viewModel
    }

}
