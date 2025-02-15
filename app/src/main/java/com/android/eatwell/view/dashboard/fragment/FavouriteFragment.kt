package com.android.eatwell.view.dashboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.eatwell.data.extension.isGPSEnabled
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.data.network.NetworkState
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.adapter.FavouriteFoodAdapter
import com.android.eatwell.view.dashboard.viewmodel.FavouriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentFavouriteBinding
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
