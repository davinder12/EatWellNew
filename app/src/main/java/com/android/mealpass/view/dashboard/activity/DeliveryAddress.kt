package com.android.mealpass.view.dashboard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.adapter.DeliveryAddressAdapter
import com.android.mealpass.view.dashboard.viewmodel.DeliveryAddressViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityDeliveryAddressBinding
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DeliveryAddress : DataBindingActivity<ActivityDeliveryAddressBinding>() {


    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 200
        const val FIVE_RECORD = 5
    }

    @Inject
    lateinit var navigation: NavigationScreen

    private val viewModel: DeliveryAddressViewModel by viewModels()

    override val layoutRes: Int get() = R.layout.activity_delivery_address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey: String = BuildConfig.PLACE_API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        binding.toolbar.menu.findItem(R.id.next).setOnMenuItemClickListener {
            if (binding.onlineSearch.text.toString().trim().isNotEmpty()) {
                navigationScreen(binding.onlineSearch.text.toString().trim())
            }
            true
        }


        subscribe(binding.saveBtn.throttleClicks()) {
            viewModel.filterMethod {
                val size = viewModel.deliveryAddressList.value?.size?.run { this < FIVE_RECORD } ?:false
                when{
                    it && size -> viewModel.updateList()
                    else -> showSnackMessage(getString(R.string.addressSaveValidation))
                }
            }
        }


     val adapter =   initAdapter(DeliveryAddressAdapter(), binding.personalAddressList, viewModel.deliveryAddressList) {
            navigationScreen(it)
        }

        subscribe(adapter.closeBtn){
            viewModel.removeItemFromList(it)
            adapter.notifyDataSetChanged()
        }


        subscribe(binding.onlineSearch.throttleClicks()) {
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            intent.putExtra("primary_color", resources.getColor(R.color.button_light_color))
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }

        subscribe(binding.clearText.throttleClicks()) {
            viewModel.isTextEditable.value = ""
        }


        subscribe(binding.cancelBtn.throttleClicks()) {
            viewModel.isManualAddressFieldOpen.value = false
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        subscribe(binding.addNewAddress.throttleClicks()) {
            viewModel.isManualAddressFieldOpen.value =
                !(viewModel.isManualAddressFieldOpen.value ?: false)
        }
    }

    private fun navigationScreen(deliveryAddress: String) {
        requirePaymentRequest()?.let {
            it.delivery_address = deliveryAddress
            it.isHomeDelivery = "1"
            navigation.goToCampaignScreen(it)

//            when {
//                it.paymentMethod.equals("campaign", ignoreCase = true) -> NavigationScreen(this
//                ).goToCampaignScreen(it)
//                else -> NavigationScreen(this).goToPaymentScreen(it)
//            }
        }


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    Autocomplete.getPlaceFromIntent(it).address?.let {
                        viewModel.isTextEditable.value = it
                    }
                }
            }
        }
    }

    override fun onBindView(binding: ActivityDeliveryAddressBinding) {
        binding.vm = viewModel
    }
}
