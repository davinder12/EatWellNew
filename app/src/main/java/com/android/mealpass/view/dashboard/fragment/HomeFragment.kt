package com.android.mealpass.view.dashboard.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.mealpass.data.extension.isGPSEnabled
import com.android.mealpass.data.extension.onTextChange
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.data.service.MealPassFirebaseMessagingService
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.adapter.AdsAdapter
import com.android.mealpass.view.dashboard.adapter.FoodAdapter
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter
import com.android.mealpass.view.dashboard.viewmodel.FindFoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentHomeBinding
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseListFragment<FragmentHomeBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_home

    companion object {
        const val CURRENT_LIST_SIZE = 5
        const val OFFSET = 0
        const val CATEGORY = "all"
        const val RESTURANT_OPEN = "1"
        const val RESTURANT_CLOSE = "0"
    }

    var NUM_PAGES =0


    @Inject
    lateinit var navigation: NavigationScreen

    private val viewModel: FindFoodViewModel by viewModels()

    private var launchProductDetailScreen : ActivityResultLauncher<Intent>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // Dont change its position
        launchProductDetailScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.updateNotificationCounter()
            searchItem(binding.searchText.text.toString())
        }

        pushNotificationHandling()
        clickListener()
        serverBadgeUpdate()

        searchItem(binding.searchText.text.toString())

        subscribe(binding.notificaiton.throttleClicks()) {
            launchProductDetailScreen?.launch(navigation.goToNotificationScreenWithCallBack())
        }

        subscribe(binding.notificationLayout.throttleClicks()){
            launchProductDetailScreen?.launch(navigation.goToNotificationScreenWithCallBack())
        }


       val listAdapter = FoodAdapter()
       initAdapter(listAdapter, binding.resturantList, viewModel.foodResource) { items ->
           listAdapter.currentList?.size?.let { viewModel.listSize = it }
           launchProductDetailScreen?.launch(
               navigation.productDetailScreenWithCallBack(
                   items.id,
                   items.storename
               )
           )
       }

        viewModel.foodApiNetworkState.observe(viewLifecycleOwner, {
            when {
                it == NetworkState.success && viewModel.isNeedToUpdateDataFirstTime -> viewModel.locationUpdateApi()
                it == NetworkState.success && viewModel.isNeedToUpdateAdsList -> viewModel.updateAdsList()
            }
        })

        viewModel.adsNetworkState.observe(viewLifecycleOwner, {
            if (it == NetworkState.success) {
                viewModel.isNeedToUpdateAdsList = false
            }
        })


        initAdapter(AdsAdapter(), binding.adsPager, viewModel.adsList, listSize = { listSize ->
            if (listSize > 0) {
                binding.adsPager.visibility = View.VISIBLE
                NUM_PAGES = listSize
                updateScrolling()
            }
        }) {
            navigation.goToSocialPage(it.link)
        }
    }

    private fun serverBadgeUpdate() {
        LocalBroadcastManager.getInstance(requireContext()).also {
            it.registerReceiver(object :BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {
                   intent?.getIntExtra(MealPassFirebaseMessagingService.BADGES_COUNT,0)?.let {
                       viewModel.updateNotificationCounter()
                   }
                }
            },IntentFilter(MealPassFirebaseMessagingService.BADGES_COUNT))
        }
    }

    private fun clickListener(){
        subscribe(binding.searchText.onTextChange(1)) {
            if(viewModel.isFirstTimeLoadData){
                viewModel.isFirstTimeLoadData = false
            }else searchItem(it.toString())
        }

        subscribe(binding.customerSupport.throttleClicks()){
            navigation.goToCall(BuildConfig.SUPPORT_NUMBER)
        }

        subscribe(binding.filterBtn.throttleClicks()) {
            val resturantFilter = FoodFilter.create {
                searchItem(binding.searchText.text.toString())
            }
            resturantFilter.show(childFragmentManager, resturantFilter.tag)
        }

        subscribe(emptyView.throttleClicks()) {
            searchItem(binding.searchText.text.toString())
        }
    }



    private fun pushNotificationHandling() {
        val bundle = arguments
        val generalNotification = bundle?.getInt(
            MealPassFirebaseMessagingService.GENERAL_NOTIFICATION_SCREEN,
            0
        )
        val productDetailScreen = bundle?.getInt(
            MealPassFirebaseMessagingService.PRODUCT_DETAIL_SCREEN,
            0
        )
        when {
            generalNotification == MealPassFirebaseMessagingService.GENERAL_NOTIFICATION_SCREEN_TYPE -> {
                navigation.goToGeneralNotification(
                    bundle.getString(MealPassFirebaseMessagingService.MESSAGE),
                    bundle.getString(MealPassFirebaseMessagingService.MERCHANT_LOGO)
                )
            }
            productDetailScreen == MealPassFirebaseMessagingService.PRODUCT_DETAIL_SCREEN_TYPE ->
            {
                launchProductDetailScreen?.launch(
                    navigation.productDetailScreenWithCallBack(
                        bundle.getString(MealPassFirebaseMessagingService.RESTURANT_ID),
                        bundle.getString(MealPassFirebaseMessagingService.RESTURANT_NAME),
                        bundle.getString(MealPassFirebaseMessagingService.NOTIFICATION_ID)
                    )
                )
            }
        }
    }


    private fun updateScrolling(){
        var currentPage = 0
        val swipeTimer = Timer()
         swipeTimer.schedule(object : TimerTask() {
             override fun run() {
                 CoroutineScope(Dispatchers.Main).launch {
                     if (currentPage < NUM_PAGES) {
                         binding.adsPager.setCurrentItem(++currentPage, true)
                     } else {
                         currentPage = 0
                         binding.adsPager.setCurrentItem(currentPage, true)
                     }
                 }
             }
         }, 8000, 8000)
    }

    private fun searchItem(item: String) {
        when {
            isAllGranted(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) -> apiHit(item)
            else -> runWithPermissions(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) { apiHit(item) }
        }
    }

    private fun apiHit(item: String) {
        when {
            !requireActivity().isGPSEnabled() -> locationSnackMessage()
             viewModel.foodResource.networkState.value != NetworkState.loading -> viewModel.searchText(
                 item
             )
        }
    }

    override fun onBindView(binding: FragmentHomeBinding) {
        binding.vm  = viewModel
    }

}

