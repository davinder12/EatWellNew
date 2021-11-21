package com.android.mealpass.view.units

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.data.service.MealPassFirebaseMessagingService
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.activity.ActiveReceiptDetail
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import org.threeten.bp.Instant
import javax.inject.Inject


abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}

@AndroidEntryPoint
class ReminderOrderNotification : HiltBroadcastReceiver() {

    @Inject
    lateinit var preference: PreferenceService

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (!preference.getString(R.string.pkey_user_Id).isNullOrEmpty()) {
            //val pendingIntent = pendingIntent(context,intent)
            showNotification(context, MealPassFirebaseMessagingService.Notification(context.getString(R.string.Reminder),
                    context.getString(R.string.PickUpOrder_Reminder), context.getString(R.string.app_name)))
        }
    }

    private fun showNotification(context: Context, notification: MealPassFirebaseMessagingService.Notification) {

        val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, notification.channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                        .setContentTitle(notification.title)
                        .setContentText(notification.body)
                        .setAutoCancel(true)

        notification.intent?.let { notificationBuilder.setContentIntent(it) }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notification.channelId, notification.channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }
        val uniqueId = Instant.now().epochSecond.toInt()
        notificationManager?.notify(uniqueId, notificationBuilder.build())
    }


    private fun pendingIntent(context: Context, intent: Intent): PendingIntent? {
        val finalIntent = requireActiveFoodReceipt(intent)?.let { data ->
            Intent(context, ActiveReceiptDetail::class.java).let {
                it.putExtra(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL, data)
                it.putExtra(NavigationScreen.PAYMENT_COMPLETE, true)
            }
        }
        return PendingIntent.getActivity(context, 0, finalIntent, PendingIntent.FLAG_CANCEL_CURRENT)

    }

    private fun requireActiveFoodReceipt(intent: Intent): ReceiptResponse.Body.ActiveReceipt? {
        var activeReceiptRespose: ReceiptResponse.Body.ActiveReceipt? = null
        intent.getBundleExtra(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL)?.getParcelable<ReceiptResponse.Body.ActiveReceipt>(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL)?.let {
            activeReceiptRespose = it
        }
        return activeReceiptRespose
    }

}