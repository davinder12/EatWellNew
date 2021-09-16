package com.android.mealpass.view.dashboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.mealpass.data.extension.isGPSEnabled
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.adapter.FavouriteFoodAdapter
import com.android.mealpass.view.dashboard.adapter.FoodAdapter
import com.android.mealpass.view.dashboard.viewmodel.FavouriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentFavouriteBinding
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : BaseListFragment<FragmentFavouriteBinding>() {


    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_favourite

    private val viewModel: FavouriteFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFavouriteList()

        val launchProductDetailScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fetchFavouriteList()
            }
        }

        initAdapter(FavouriteFoodAdapter(), binding.itemDisplay, viewModel.favouriteItemListData,
                viewModel.favouriteItemList) { items ->
            launchProductDetailScreen.launch(navigationScreen.productDetailScreenWithCallBack(items.id, items.storename))
        }
        subscribe(emptyView.throttleClicks()) {
            fetchFavouriteList()
        }

    }


    private fun fetchFavouriteList() {
        when {
            isAllGranted(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) -> apiHit()
            else -> runWithPermissions(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) { apiHit() }
        }
    }

    private fun apiHit() {
        when {
            !requireActivity().isGPSEnabled() -> locationSnackMessage()
            viewModel.favouriteItemList.networkState.value != NetworkState.loading -> {
                viewModel.updateLocation()
            }
        }
    }

}
