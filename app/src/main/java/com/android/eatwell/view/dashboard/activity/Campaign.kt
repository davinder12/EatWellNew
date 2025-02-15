package com.android.eatwell.view.dashboard.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.viewmodel.CampaignViewModel
import com.android.eatwell.view.units.ReminderOrderNotification
import com.android.eatwell.view.units.getLocatDate
import com.android.eatwell.view.units.isLocalTimeGreaterThan60Min
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityCampaignCodeBinding
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
            PendingIntent.getBroadcast(this, 0, intent, 0 or FLAG_IMMUTABLE)

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
