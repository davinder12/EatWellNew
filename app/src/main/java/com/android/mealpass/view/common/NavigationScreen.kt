package com.android.mealpass.view.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.mealpass.data.models.ReceiptResponse
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.view.dashboard.DashboardActivity
import com.android.mealpass.view.dashboard.activity.Campaign
import com.android.mealpass.view.dashboard.activity.DeliveryAddress
import com.android.mealpass.view.dashboard.activity.ProductDetail
import com.android.mealpass.view.dashboard.activity.UsedReceiptDetail
import com.android.mealpass.view.dashboard.fragment.setting.ActivityFeedBack
import com.android.mealpass.view.dashboard.fragment.setting.ChangePasswordActivity
import com.android.mealpass.view.dashboard.fragment.setting.ProfileActivity
import com.android.mealpass.view.dashboard.fragment.setting.ReferalCodeActivity
import com.android.mealpass.view.login.StartUpActivity
import com.android.mealpass.view.merchant.MerchantActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class NavigationScreen @Inject constructor(@ActivityContext private val context: Context) {

    companion object {
        const val URL = "url"
        const val TITLE = "title"
        const val NOTIFICATION = "From Notification"
        const val RESTURANT_ID = "resturant_id"
        const val RESTURANT_Name = "resturant_name"
        const val EXTRA_PAYMENT = "EXTRA_PAYMENT"
        const val EXTRA_ACTIVE_RECEIPT_DETAIL = "EXTRA_ACTIVE_RECEIPT_DETAIl"
        const val IS_FIRST_TIME_VISIT = "IS_FIRST_TIME_VISIT"
        const val IS_SOCIAL_LOGIN ="IS_SOCIAL_LOGIN"
        
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

    fun goToUsedReceiptDetailActivity(usedReceipt: ReceiptResponse.Body.UsedReceipt) {
        val intent = Intent(context, UsedReceiptDetail::class.java).also {
            it.putExtra(EXTRA_ACTIVE_RECEIPT_DETAIL, usedReceipt)
        }
        context.startActivity(intent)
    }

    fun goToDashBoard() {
       context.startActivity(Intent(context,DashboardActivity::class.java))
    }


    fun goToReferralCodeScreen(isFirstTimeVisit: Boolean = false) {
        Intent(context, ReferalCodeActivity::class.java).also {
            it.putExtra(IS_FIRST_TIME_VISIT,isFirstTimeVisit)
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
        context.startActivity(Intent(context,StartUpActivity::class.java))
        ActivityCompat.finishAffinity(appCompatActivity)
    }

    fun goToStartUpScreen(){
        context.startActivity(Intent(context,StartUpActivity::class.java))
    }

    fun goToSocialPage(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context.startActivity(i)

    }





}
