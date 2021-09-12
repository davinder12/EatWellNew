package com.android.mealpass.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkBuilder
import com.android.mealpass.view.dashboard.DashboardActivity
import com.android.mealpass.view.merchant.MerchantActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import org.threeten.bp.Instant
import javax.inject.Inject


@AndroidEntryPoint
class MealPassFirebaseMessagingService  : FirebaseMessagingService() {


    @Inject
    lateinit var preferenceService: PreferenceService


    companion object {
        const val MERCHANT_NOTIFICATION_SCREEN = "product_order_push"
        const val PRODUCT_DETAIL_SCREEN = "1"
        const val GENERAL_NOTIFICATION_SCREEN = "3"
        const val TYPE = "type"
        const val MEAlPASS ="MealPass"
        const val MESSAGE ="message"
        const val MERCHANT_LOGO = "merchant_logo"
        const val RESTURANT_ID ="user_id"
        const val RESTURANT_NAME ="username"
        const val NOTIFICATION_ID ="notification_id"
        const val PRODUCT_DETAIL_SCREEN_TYPE = 1
        const val GENERAL_NOTIFICATION_SCREEN_TYPE = 3
        const val BADGES_COUNT = "badge_count"
        const val MERCHANT_BROADCAST ="MERCHANT_BROADCAST"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        when{
//             preferenceService.getString(R.string.pkey_user_Id).isNullOrEmpty() -> showNotification(Notification(MEAlPASS, getData(MESSAGE,remoteMessage),getString(R.string.app_name),null))
//             else ->  handleMessage(remoteMessage)
//        }

        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        val channelId = getString(R.string.app_name)
        try {
             remoteMessage.data[TYPE]?.let { type ->
              val pendingIntent =  when (type) {
                  MERCHANT_NOTIFICATION_SCREEN -> getMerchantNotification()
                  PRODUCT_DETAIL_SCREEN -> getProductDetailIntent(remoteMessage)
                  GENERAL_NOTIFICATION_SCREEN -> getGeneralNotificationIntent(remoteMessage)
                     else -> null
               }
                 showNotification(
                     Notification(
                         MEAlPASS,
                         getData(MESSAGE, remoteMessage),
                         channelId,
                         pendingIntent
                     )
                 )
             }
        } catch (e: Exception) { }
    }


    private  fun getMerchantNotification(): PendingIntent {
        notifyToMerchant()

          return NavDeepLinkBuilder(applicationContext)
               .setGraph(R.navigation.nav_merchant)
               .setDestination(R.id.merchantNotification)
               .setComponentName(MerchantActivity::class.java)
               .createPendingIntent()


        //Intent(applicationContext, MerchantNotification::class.java).also{ it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
    }

    private fun getGeneralNotificationIntent(remoteMessage: RemoteMessage): PendingIntent {
//       return Intent(applicationContext, DashboardActivity::class.java).also {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            it.putExtra(MESSAGE, getData(MESSAGE,remoteMessage))
//            it.putExtra(MERCHANT_LOGO, getData(MERCHANT_LOGO,remoteMessage))
//            it.putExtra(GENERAL_NOTIFICATION_SCREEN,GENERAL_NOTIFICATION_SCREEN_TYPE)

          val bundle = Bundle().also {
              it.putString(MESSAGE, getData(MESSAGE, remoteMessage))
              it.putString(MERCHANT_LOGO, getData(MERCHANT_LOGO, remoteMessage))
              it.putInt(GENERAL_NOTIFICATION_SCREEN, GENERAL_NOTIFICATION_SCREEN_TYPE)
          }
           return NavDeepLinkBuilder(applicationContext)
                   .setGraph(R.navigation.nav_dashboard)
                   .setDestination(R.id.navigation_home)
                   .setComponentName(DashboardActivity::class.java)
                   .setArguments(bundle)
                   .createPendingIntent()

  //     }
    }

    private fun getProductDetailIntent(remoteMessage: RemoteMessage): PendingIntent {
        val bundle = Bundle().also {
            it.putString(MESSAGE, getData(MESSAGE, remoteMessage))
            it.putString(RESTURANT_ID, getData(RESTURANT_ID, remoteMessage))
            it.putString(RESTURANT_NAME, getData(RESTURANT_NAME, remoteMessage))
            it.putString(NOTIFICATION_ID, getData(NOTIFICATION_ID, remoteMessage))
            it.putInt(PRODUCT_DETAIL_SCREEN, PRODUCT_DETAIL_SCREEN_TYPE)
           // it.putString(BADGES_COUNT, getNumberData(BADGES_COUNT, remoteMessage))
            preferenceService.putInt(R.string.pkey_notification_count,getNumberData(BADGES_COUNT, remoteMessage)?.toInt()?:0)
            updateBadges(getNumberData(BADGES_COUNT, remoteMessage))
        }
        return NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.nav_dashboard)
                .setDestination(R.id.navigation_home)
                .setComponentName(DashboardActivity::class.java)
                .setArguments(bundle)
                .createPendingIntent()


//        return Intent(applicationContext, DashboardActivity::class.java).also {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            it.putExtra(MESSAGE, getData(MESSAGE,remoteMessage))
//            it.putExtra(RESTURANT_ID, getData(RESTURANT_ID,remoteMessage))
//            it.putExtra(RESTURANT_NAME, getData(RESTURANT_NAME,remoteMessage))
//            it.putExtra(NOTIFICATION_ID, getData(NOTIFICATION_ID,remoteMessage))
//            it.putExtra(PRODUCT_DETAIL_SCREEN,PRODUCT_DETAIL_SCREEN_TYPE)
//            it.putExtra(BADGES_COUNT,getNumberData(BADGES_COUNT,remoteMessage))
//        }
    }

    private fun notifyToMerchant() {
        Intent(MERCHANT_BROADCAST).also {
            LocalBroadcastManager.getInstance(this).sendBroadcast(it)
        }
    }


    private fun updateBadges(numberData: String?) {
        numberData?.toIntOrNull()?.let { count->
            Intent(BADGES_COUNT).also {
                it.putExtra(BADGES_COUNT,count)
                LocalBroadcastManager.getInstance(this).sendBroadcast(it)
            }
        }
    }


    private fun getPendingIntent(intent: Intent?): PendingIntent? {
        var pendingIntent : PendingIntent? = null
        intent?.let { it -> pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            it,
            PendingIntent.FLAG_CANCEL_CURRENT
        ) }
        return pendingIntent
    }

    private fun getData(key: String, remoteMessage: RemoteMessage): String? {
        return remoteMessage.data.getOrElse(key, { "" })
    }

    private fun getNumberData(key: String, remoteMessage: RemoteMessage): String? {
        return remoteMessage.data.getOrElse(key,  {"0"} )
    }


    private fun showNotification(notification: Notification) {
        val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, notification.channelId)
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
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notification.channelId,
                notification.channelId,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(channel)
         }
        val uniqueId = Instant.now().epochSecond.toInt()
        notificationManager?.notify(uniqueId, notificationBuilder.build())
    }

     data class Notification(
         val title: String?,
         val body: String?,
         val channelId: String,
         val intent: PendingIntent? = null
     )

    override fun onNewToken(token: String) {

    }
}
