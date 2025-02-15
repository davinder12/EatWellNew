package com.android.eatwell.view.units

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.android.eatwell.data.service.MealPassFirebaseMessagingService
import com.android.eatwell.data.service.PreferenceService
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import org.threeten.bp.Instant
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preference: PreferenceService

    override fun onReceive(context: Context, intent: Intent) {
        notificationMethod(context, preference)
    }
    private fun notificationMethod(context: Context, preferenceService: PreferenceService) {
        if (!preferenceService.getBoolean(R.string.pkey_local_notification) &&
            preferenceService.getBoolean(R.string.pkey_allow_daily_push))
        {
         val message =   if(preferenceService.getString((R.string.pkey_daily_message)).isNullOrEmpty()) context.getString(R.string.DailyPushMsg)
             else preferenceService.getString(R.string.pkey_daily_message)

        showNotification(context,MealPassFirebaseMessagingService.Notification(context.getString(R.string.app_name),message,context.getString(R.string.app_name)))
        }
    }


    private fun showNotification(context :Context, notification: MealPassFirebaseMessagingService.Notification) {
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, notification.channelId)
                .setSmallIcon(R.drawable.logo)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
//                        .setLights(
//                                ContextCompat.getColor(app, R.color.hoopit),
//                                NOTIFICATION_LED_ON_MS, NOTIFICATION_LED_OFF_MS
//                        )
        notification.intent?.let { notificationBuilder.setContentIntent(it) }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notification.channelId, notification.channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }
        val uniqueId = Instant.now().epochSecond.toInt()
        notificationManager?.notify(uniqueId, notificationBuilder.build())
    }


}