package com.android.mealpass.view.dashboard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.mealpass.data.api.enums.ProductBuyEnum
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.models.ProductItem
import com.android.mealpass.data.models.SpecificFoodResponse
import com.android.mealpass.data.service.MealPassFirebaseMessagingService
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.common.NavigationScreen.Companion.RESTURANT_ID
import com.android.mealpass.view.common.NavigationScreen.Companion.RESTURANT_Name
import com.android.mealpass.view.dashboard.activity.dialog.OrderSelectionDialog
import com.android.mealpass.view.dashboard.activity.dialog.PhoneNumberDialog
import com.android.mealpass.view.dashboard.viewmodel.ProductDetailViewModel
import com.android.mealpass.widgets.alertDialog
import com.android.mealpass.widgets.campignDialog
import com.android.mealpass.widgets.messageDialog
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityProductDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetail : DataBindingActivity<ActivityProductDetailBinding>() {


    @Inject
    lateinit var navigationScreen: NavigationScreen

    private val viewModel: ProductDetailViewModel by viewModels()

    companion object {
        const val DEFAULT_QTY = 0
        const val FOR_DONATION_ONLY = 1
        const val CAMPAIGN = "campaign"
        const val STAFF = "staffMember"
        const val STAFF_MEMBER = 1
        const val NOT_STAFF_MEMBER = 0
    }

    override val layoutRes: Int
        get() = R.layout.activity_product_detail



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val favourite =  binding.toolbar.menu.findItem(R.id.favourites)

        binding.toolbar.setNavigationOnClickListener {
           onBackPressed()
        }

       subscribe(binding.markerLayout.throttleClicks()){
           viewModel.latitude?.let { lat ->
               viewModel.longitude?.let { lng ->
                   navigationScreen.goToGoogleMap(lat,lng)
               }
           }
       }

       subscribe(binding.siteLayout.throttleClicks()){
           navigationScreen.goToSocialPage(viewModel.webSiteUrl)
       }

        viewModel.isFavouriteLike.observe(this) {
            it?.let { currentLike ->
                favourite.isChecked = currentLike
                val drawable =
                    if (currentLike) R.drawable.ic_white_favourite else R.drawable.ic_trans_favorite
                favourite.icon = ResourcesCompat.getDrawable(resources, drawable, null)
            }
        }

        favourite?.let { item ->
            item.setOnMenuItemClickListener {
                  when {
                      favourite.isChecked -> {
                          bindNetworkState(viewModel.removeToFavourite()) {
                              item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_trans_favorite, null)
                              favourite.isChecked = !it.isChecked
                              viewModel.resultCode = Activity.RESULT_OK
                          }
                      } else ->{
                       bindNetworkState(viewModel.addToFavourite()){
                       item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_white_favourite, null)
                       favourite.isChecked = !it.isChecked
                       viewModel.resultCode = Activity.RESULT_OK
                      } }
                  }
                true
            }
        }

        subscribe(binding.addItem.throttleClicks()) {
            viewModel.resturantRequest.data.value?.let { specificFoodResponse ->
                when (viewModel.updatePortionValidation(specificFoodResponse.body)) {
                    ProductBuyEnum.SHOP_CLOSE -> showMessage(getString(R.string.closeorderlable))
                    ProductBuyEnum.FOR_STAFF_MEMBER_ONLY -> showMessage(getString(R.string.staffMemberValidation))
                    ProductBuyEnum.NEED_PHONENUMBER -> {
                        val phoneNumberDialog = PhoneNumberDialog.create {
                            val orderSelectionDialog = OrderSelectionDialog.create(specificFoodResponse.body.itemleft) {
                                specificFoodResponse.body.defaultPortion = it
                                portionValidation(specificFoodResponse)
                            }
                            orderSelectionDialog.show(supportFragmentManager, orderSelectionDialog.tag)
                        }
                        phoneNumberDialog.show(supportFragmentManager, phoneNumberDialog.tag)
                    }
                    else -> {
                        val orderSelectionDialog =
                            OrderSelectionDialog.create(specificFoodResponse.body.itemleft) {
                                specificFoodResponse.body.defaultPortion = it
                                portionValidation(specificFoodResponse)
                            }
                        orderSelectionDialog.show(supportFragmentManager, orderSelectionDialog.tag)
                    }
                }
            }
        }

        viewModel.description.observe(this) {
            binding.itemDescription.loadData(it, "text/html; charset=utf-8", "utf-8")
        }

        intent.extras?.getString(RESTURANT_Name, "")?.let {
            viewModel.resturantName.value = it
        }
        intent.extras?.getString(RESTURANT_ID, "")?.let {
            viewModel.updateResturantId(it)
        }

        intent.extras?.getString(MealPassFirebaseMessagingService.NOTIFICATION_ID, "")?.let {
           if(it.isNotEmpty()) viewModel.updateBadges()
            viewModel.notificationId = it
        }

        viewModel.resturantDrawable.observe(this) {
            binding.addItem.setImageDrawable(ContextCompat.getDrawable(this, it))
        }

        bindNetworkState(viewModel.networkState,loadingIndicator =  binding.productDetailProgress,onError = {
            finish()
        })

    }

    private fun portionValidation(specificFoodResponse: SpecificFoodResponse) {
        val result = viewModel.validationProductInfo(specificFoodResponse.body)
        when (result.productBuyEnum) {
            ProductBuyEnum.DONATION_PORTION_EMPTY, ProductBuyEnum.STAFF_PORTION_EMPTY -> messageDialog(getString(R.string.MaximumLimitOver))
            ProductBuyEnum.TOTAL_STAFF_PORTION_EMPTY, ProductBuyEnum.TOTAL_DONATION_PORTION_LIMIT_OVER -> {
                messageDialog(getString(R.string.RestaurantLimitOver))
            }
            ProductBuyEnum.DONATION_PORTION, ProductBuyEnum.STAFF_PORTION -> {
                campignDialog(getString(R.string.ConfirmProduct, result.quantity.toString()) + getString(
                        R.string.OrderConfirmation
                )) {
                    result.campaignUserHomeDelivery(
                            result.saveReceiptRequestModel?.isDelivery ?: false,
                            getString(R.string.Delivery),
                            getString(R.string.DeliveryDonationPopUp)
                    )
                }
            }
            ProductBuyEnum.DONATION_PORTION_LIMIT_OVER, ProductBuyEnum.STAFF_PORTION_LIMIT_OVER -> {
                getMessage(result)
                campignDialog(getMessage(result)) {
                    result.campaignUserHomeDelivery(
                            result.saveReceiptRequestModel?.isDelivery ?: false,
                            getString(R.string.Delivery),
                            getString(R.string.DeliveryDonationPopUp)
                    )
                }
            }
            else -> {
                showMessage(getString(R.string.not_campaign_user))
            }
        }
    }

    private fun getMessage(productItem: ProductItem): String {
        return if (productItem.productBuyEnum == ProductBuyEnum.STAFF_PORTION_LIMIT_OVER)
            getString(R.string.StaffPortionLeft, productItem.quantity.toString(), productItem.quantity.toString())
        else getString(R.string.PortionLeft, productItem.quantity.toString(), productItem.quantity.toString())

    }


    private fun ProductItem.campaignUserHomeDelivery(
            isDelivery: Boolean,
            title: String,
            message: String
    ) {
        when {
            isDelivery -> {
                alertDialog(title,
                        message,
                        getString(R.string.Continue),
                        getString(R.string.Cancel),
                        successResponse = {
                            navigationScreen.goToDeliveryScreen(this.saveReceiptRequestModel)
                        },
                        errorResponse = {
                            this.saveReceiptRequestModel?.isDelivery = false
                            this.saveReceiptRequestModel?.delivery_amount = 0f
                            navigationScreen.goToCampaignScreen(this.saveReceiptRequestModel)
                        }
                )
            }
            else -> navigationScreen.goToCampaignScreen(this.saveReceiptRequestModel)
        }
    }


    override fun onBindView(binding: ActivityProductDetailBinding) {
        binding.vm = viewModel
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent().also { setResult(viewModel.resultCode, it) }
        finish()
    }

}