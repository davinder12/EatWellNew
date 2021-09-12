package com.android.mealpass.view.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.service.MealPassFirebaseMessagingService.Companion.MERCHANT_LOGO
import com.android.mealpass.data.service.MealPassFirebaseMessagingService.Companion.MESSAGE
import com.android.mealpass.data.service.MealPassFirebaseMessagingService.Companion.NOTIFICATION_ID
import com.android.mealpass.view.dashboard.DashboardActivity
import com.android.mealpass.view.dashboard.activity.*
import com.android.mealpass.view.dashboard.fragment.setting.ActivityFeedBack
import com.android.mealpass.view.dashboard.fragment.setting.ChangePasswordActivity
import com.android.mealpass.view.dashboard.fragment.setting.ProfileActivity
import com.android.mealpass.view.dashboard.fragment.setting.ReferalCodeActivity
import com.android.mealpass.view.login.StartUpActivity
import com.android.mealpass.view.login.TermConditionActivity
import com.android.mealpass.view.merchant.MerchantActivity
import com.android.mealpass.view.notification.GeneralNotificationActivity
import com.android.mealpass.view.notification.NotificationActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class NavigationScreen @Inject constructor(@ActivityContext private val context: Context) {

    companion object {

        const val RESTURANT_ID = "resturant_id"
        const val RESTURANT_Name = "resturant_name"
        const val EXTRA_PAYMENT = "EXTRA_PAYMENT"
        const val EXTRA_ACTIVE_RECEIPT_DETAIL = "EXTRA_ACTIVE_RECEIPT_DETAIl"
        const val IS_FIRST_TIME_VISIT = "IS_FIRST_TIME_VISIT"
        const val IS_SOCIAL_LOGIN ="IS_SOCIAL_LOGIN"
        const val PAYMENT_COMPLETE = "EXTRA_PAYMENT_COMPLETE"
        const val MERCHANT_LOGIN = "EXTRA_MERCHANT_LOGIN"

    }


    fun dashBoardScreenNavigation() {
        val intent = Intent(context, DashboardActivity::class.java)
        context.startActivity(intent)
    }


    fun goToMerchantScreen() {
        val intent = Intent(context, MerchantActivity::class.java)
        context.startActivity(intent)
    }

    fun productDetailScreen(resturantId: String?, resturantName: String?) {
        val intent = Intent(context, ProductDetail::class.java)
        intent.putExtra(RESTURANT_ID, resturantId)
        intent.putExtra(RESTURANT_Name, resturantName)
        context.startActivity(intent)
    }

    fun goToGeneralNotification(message: String?, merchantLogo: String?) {
        val intent = Intent(context, GeneralNotificationActivity::class.java)
        intent.putExtra(MESSAGE, message)
        intent.putExtra(MERCHANT_LOGO, merchantLogo)
        context.startActivity(intent)
    }


    fun productDetailScreenWithCallBack(resturantId: String?, resturantName: String?,notificationId:String?= ""): Intent {
        val intent = Intent(context, ProductDetail::class.java)
        intent.putExtra(RESTURANT_ID, resturantId)
        intent.putExtra(RESTURANT_Name, resturantName)
        intent.putExtra(NOTIFICATION_ID, notificationId)
        return intent
    }

    fun goToDeliveryScreen(saveReceiptRequestModel: SaveReceiptRequestModel?) {
        val intent = Intent(context, DeliveryAddress::class.java).also {
            it.putExtra(EXTRA_PAYMENT, saveReceiptRequestModel)
        }
        context.startActivity(intent)
    }

    fun goToCampaignScreen(saveReceiptRequestModel: SaveReceiptRequestModel?) {
        val intent = Intent(context, Campaign::class.java).also {
            it.putExtra(EXTRA_PAYMENT, saveReceiptRequestModel)
        }
        context.startActivity(intent)
    }

    fun goToActiveReceipt(data: ReceiptResponse.Body.ActiveReceipt, isPaymentComplete: Boolean = false) : Intent {
      return  Intent(context, ActiveReceiptDetail::class.java).let {
             it.putExtra(EXTRA_ACTIVE_RECEIPT_DETAIL, data)
             it.putExtra(PAYMENT_COMPLETE, isPaymentComplete)
        }
    }

    fun goToUsedReceiptDetailActivity(usedReceipt: ReceiptResponse.Body.UsedReceipt) {
        val intent = Intent(context, UsedReceiptDetail::class.java).also {
            it.putExtra(EXTRA_ACTIVE_RECEIPT_DETAIL, usedReceipt)
        }
        context.startActivity(intent)
    }

    fun goToDashBoard() {
       context.startActivity(Intent(context, DashboardActivity::class.java))
    }


    fun goToReferralCodeScreen(isFirstTimeVisit: Boolean = false) {
        Intent(context, ReferalCodeActivity::class.java).also {
            it.putExtra(IS_FIRST_TIME_VISIT, isFirstTimeVisit)
            context.startActivity(it)
        }
    }

    fun gotToProfileActivity() {
        Intent(context, ProfileActivity::class.java).also {
            context.startActivity(it)
        }
    }

    fun goToFeedBackScreen() {
        Intent(context, ActivityFeedBack::class.java).also {
            context.startActivity(it)
        }
    }

    fun gotToChangePasswordActivity() {
        Intent(context, ChangePasswordActivity::class.java).also {
            context.startActivity(it)
        }
    }

    fun gotToResturantActivity() {
      //  activity.startActivity<ResturantActivity>()
    }

    fun goToMainScreen(appCompatActivity: AppCompatActivity) {
        context.startActivity(Intent(context, StartUpActivity::class.java))
        ActivityCompat.finishAffinity(appCompatActivity)
    }

    fun goToStartUpScreen(){
        context.startActivity(Intent(context, StartUpActivity::class.java))
    }

    fun goToTermAndConditionScreen(merchantLogin: Boolean) {
        Intent(context, TermConditionActivity::class.java).also {
            it.putExtra(MERCHANT_LOGIN,merchantLogin)
            context.startActivity(it)
        }
    }

    fun goToNotificationScreenWithCallBack(): Intent {
       return Intent(context, NotificationActivity::class.java)
    }



    fun goToSocialPage(url: String?) {
        if(!url.isNullOrEmpty()) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }



    fun goToCall(mobile: String){
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile, null))
        context.startActivity(intent)
    }

    fun goToGoogleMap(lat:String,long:String){
        val urlAddress = "http://maps.google.com/maps?q=$lat,$long(resturant)&iwloc=A&hl=es"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress))
        context.startActivity(intent)
    }





}
