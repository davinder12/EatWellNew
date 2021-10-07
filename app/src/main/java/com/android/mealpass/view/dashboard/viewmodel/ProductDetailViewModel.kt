package com.android.mealpass.view.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.api.enums.ProductBuyEnum
import com.android.mealpass.data.extension.map
import com.android.mealpass.data.extension.mutableLiveData
import com.android.mealpass.data.models.ProductItem
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.models.SingleResturantRequest
import com.android.mealpass.data.models.SpecificFoodResponse
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.repository.ProductRepository
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseclass.BaseViewModel
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.CAMPAIGN
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.DEFAULT_QTY
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.FOR_DONATION_ONLY
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.NOT_STAFF_MEMBER
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.STAFF
import com.android.mealpass.view.dashboard.activity.ProductDetail.Companion.STAFF_MEMBER
import com.android.mealpass.view.units.isHomeDeliveryAvailable
import com.android.mealpass.view.units.isResturantOpen
import com.android.mealpass.view.units.pickUpTime
import dagger.hilt.android.lifecycle.HiltViewModel
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferenceService: PreferenceService,
) : BaseViewModel() {


    var resultCode = 0
    var notificationId = ""
    var resturantName = mutableLiveData("")
    var resturantId = MutableLiveData<String>()
    var latitude :String? = ""
    var longitude :String? =""
    var webSiteUrl:String? =""



    var resturantRequest = ResourceViewModel(resturantId) {
        productRepository. getSingleResturant(
            SingleResturantRequest(
                it,
                preferenceService.getString(R.string.pkey_userlat),
                preferenceService.getString(R.string.pkey_userLong), notificationId, TimeZone.getDefault().id,
                preferenceService.getString(R.string.pkey_user_Id))
        )
    }


    fun updateBadges(){
         var numberOfCount =preferenceService.getInt(R.string.pkey_notification_count)
         if( numberOfCount > 0 ){
             numberOfCount -= 1
             preferenceService.putInt(R.string.pkey_notification_count,numberOfCount)
         }
    }


    var coverPhoto = resturantRequest.data.map {
        latitude = it.body.latitude
        longitude = it.body.longitude
        webSiteUrl = it.body.website_url
        it.body.cover_photo
    }

    var favCount = resturantRequest.data.map {
        it.body.fav_count
    }

    var needToVisibleStock = resturantRequest.data.map {
        it.status.code == 1
    }

    var campaignItemLeft = resturantRequest.data.map {
        it.body.campaign_itemleft
    }

    var logo = resturantRequest.data.map {
        it.body.logo
    }

    var openingTime = resturantRequest.data.map {
        it.body.opening_time
    }

    var closingTime = resturantRequest.data.map {
        it.body.closing_time
    }

    var itemLeft = resturantRequest.data.map {
        it.body.itemleft
    }

    var merchantTotalCampaignSell = resturantRequest.data.map {
        it.body.MerchantTotalCampaignSell
    }

    var description = resturantRequest.data.map {
        it.body.description ?: ""
    }

    var expectedDescription = resturantRequest.data.map {
        it.body.expected_description
    }

    var address = resturantRequest.data.map {
        it.body.address
    }

    var webUrl = resturantRequest.data.map {
        it.body.website_url
    }

    var resturantDrawable = resturantRequest.data.map {
        when {
            isResturantOpen(
                it.body.itemleft,
                it.body.is_open,
                it.body.opening_time,
                it.body.closing_time,
                it.body.before_pickup_time,
                it.body.shop_open_time,
                it.body.is_active
            ) -> R.drawable.ic_shopping_basket
            else -> R.drawable.ic_shopping_basket_red
        }
    }

    var isHomeDeliveryAvailable = resturantRequest.data.map {
        isHomeDelivery(it.body, "")
    }

    var isFavouriteLike = resturantRequest.data.map { it.body.isCurrentLike  == 1  }

    var networkState = resturantRequest.networkState.map {
        it
    }

    fun updateResturantId(resturantId: String) {
        this.resturantId.postValue(resturantId)
    }


    fun addToFavourite(): LiveData<NetworkState> {
        return productRepository.resturantLike(
            preferenceService.getString(R.string.pkey_user_Id),
            resturantId.value
        ).also {
            subscribe(it.request)
        }.networkState
    }

    fun removeToFavourite(): LiveData<NetworkState> {
        return productRepository.resturantUnLike(
            preferenceService.getString(R.string.pkey_user_Id),
            resturantId.value
        ).also {
            subscribe(it.request)
        }.networkState
    }





    fun updatePortionValidation(product: SpecificFoodResponse.Body): ProductBuyEnum {
        return when {
            !isResturantOpenOrClosed(product) -> ProductBuyEnum.SHOP_CLOSE
            (product.is_only_for_staff && !product.is_staff_user) && product.isOnlyforDonation != FOR_DONATION_ONLY -> ProductBuyEnum.FOR_STAFF_MEMBER_ONLY
            preferenceService.getString(R.string.pkey_phoneNumber, "").isNullOrEmpty() -> ProductBuyEnum.NEED_PHONENUMBER
            else -> ProductBuyEnum.NO_ISSUE
        }
    }


    fun validationProductInfo(product: SpecificFoodResponse.Body): ProductItem {
        val isForStaffMember = (product.is_only_for_staff && product.is_staff_user)
        return when {
            isForStaffMember -> {
                // if all staff item is empty and use is also a campaign user need to check that part too
                if (product.user_staff_food_left <= DEFAULT_QTY && product.isOnlyforDonation == FOR_DONATION_ONLY)
                    campaignPortion(product, CAMPAIGN)
                else staffPortion(product, STAFF)
            }
            product.isOnlyforDonation == FOR_DONATION_ONLY -> campaignPortion(product, CAMPAIGN)
            else -> ProductItem(ProductBuyEnum.NO_CAMPIGN_USER)
        }
    }


    private fun isResturantOpenOrClosed(product: SpecificFoodResponse.Body): Boolean {
        return isResturantOpen(
            product.itemleft,
            product.is_open,
            product.opening_time,
            product.closing_time,
            product.before_pickup_time,
            product.shop_open_time, product.is_active
        )
    }

    private fun campaignPortion(productInfo: SpecificFoodResponse.Body, paymentMethod: String): ProductItem {
        val totalCampaignItemLeft = productInfo.campaign_itemleft
        val userItemSelected: Int = productInfo.defaultPortion
        val userItemAllowed = productInfo.userCampaignPortionLeft
        return when {
            totalCampaignItemLeft <= DEFAULT_QTY -> ProductItem(ProductBuyEnum.TOTAL_DONATION_PORTION_LIMIT_OVER, null)
            userItemAllowed <= DEFAULT_QTY -> ProductItem(ProductBuyEnum.DONATION_PORTION_EMPTY, null)
            userItemSelected <= totalCampaignItemLeft && userItemSelected <= userItemAllowed -> {
                val price = userItemSelected * productInfo.price
                ProductItem(
                        ProductBuyEnum.DONATION_PORTION,
                        updateRequestModel(price, productInfo, paymentMethod, NOT_STAFF_MEMBER, userItemSelected),
                        userItemSelected
                )
            }
            else -> {
                val itemLeft = if (userItemAllowed <= totalCampaignItemLeft) userItemAllowed else totalCampaignItemLeft
                val price = itemLeft * productInfo.price
                ProductItem(ProductBuyEnum.DONATION_PORTION_LIMIT_OVER,
                        updateRequestModel(price, productInfo, paymentMethod, NOT_STAFF_MEMBER, itemLeft),
                        itemLeft
                )
            }
        }
    }

    private fun staffPortion(productInfo: SpecificFoodResponse.Body, paymentMethod: String): ProductItem {
        val totalStaffItemLeft = productInfo.merchant_staff_food_left
        val userItemSelected: Int = productInfo.defaultPortion
        val userItemAllowed = productInfo.user_staff_food_left
        return when {
            totalStaffItemLeft <= DEFAULT_QTY -> ProductItem(ProductBuyEnum.TOTAL_STAFF_PORTION_EMPTY, null)
            userItemAllowed <= DEFAULT_QTY -> ProductItem(ProductBuyEnum.STAFF_PORTION_EMPTY, null)
            userItemSelected <= totalStaffItemLeft && userItemSelected <= userItemAllowed -> {
                val price = userItemSelected * productInfo.price
                ProductItem(
                        ProductBuyEnum.STAFF_PORTION,
                        updateRequestModel(price, productInfo, paymentMethod, STAFF_MEMBER, userItemSelected),
                        userItemSelected
                )
            }
            else -> {
                val itemLeft = if (userItemAllowed <= totalStaffItemLeft) userItemAllowed else totalStaffItemLeft
                val price = itemLeft * productInfo.price
                ProductItem(ProductBuyEnum.STAFF_PORTION_LIMIT_OVER,
                        updateRequestModel(price, productInfo, paymentMethod, STAFF_MEMBER, itemLeft), itemLeft)
            }
        }
    }

    // Receipt Request Model
    private fun updateRequestModel(
            amount: Float,
            productInfo: SpecificFoodResponse.Body,
            paymentInfo: String,
            isStaffReceipt: Int,
            userItemSelected: Int
    ): SaveReceiptRequestModel {

        val resturantId = productInfo.res_id
        var deliveryAmount = 0f
        var isdelivery = false

        if (isHomeDelivery(productInfo, paymentInfo)) {
            deliveryAmount = productInfo.delivery_amount
            isdelivery = true
        }

        return SaveReceiptRequestModel(
                amount, resturantId, preferenceService.getString(R.string.pkey_user_Id),
                productInfo.opening_time, productInfo.closing_time,
                listOf(
                        SaveReceiptRequestModel.ReceiptProductInfo(
                                amount,
                                productInfo.before_price,
                                productInfo.price,
                                "",   // productInfo.product_id, not sure cross verify for it
                                userItemSelected
                        )
                ), paymentInfo,
                deliveryAmount,
                isdelivery,
                productInfo.currency_type, userItemSelected,
                paymentMethod = paymentInfo,
                isFromCampaign = if (paymentInfo == CAMPAIGN) "1" else "0",
                collection_time = pickUpTime(productInfo.opening_time, productInfo.closing_time), isStaffReceipt = isStaffReceipt
        )
    }


    private fun isHomeDelivery(
        productInfo: SpecificFoodResponse.Body,
        paymentInfo: String
    ): Boolean {

        val condition = isHomeDeliveryAvailable(productInfo.is_home_delivery,productInfo.allow_fullday_delivery,
                productInfo.opening_time,productInfo.delivery_close_before_hours)
        return if (paymentInfo == CAMPAIGN || paymentInfo == STAFF) productInfo.isFreeDelivery == 1 && condition else condition
    }


}