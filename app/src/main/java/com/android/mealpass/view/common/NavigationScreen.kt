package com.android.mealpass.view.common

import android.content.Context
import android.content.Intent
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.view.dashboard.DashboardActivity
import com.android.mealpass.view.dashboard.activity.Campaign
import com.android.mealpass.view.dashboard.activity.DeliveryAddress
import com.android.mealpass.view.dashboard.activity.ProductDetail
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
    }

    fun dashBoardScreenNavigation() {
        val intent = Intent(context, DashboardActivity::class.java)
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


}
