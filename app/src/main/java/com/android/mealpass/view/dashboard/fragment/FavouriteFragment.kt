package com.android.mealpass.view.dashboard.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.mealpass.data.extension.isGPSEnabled
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.dashboard.adapter.FoodAdapter
import com.android.mealpass.view.dashboard.viewmodel.FavouriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_view.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentFavouriteBinding

@AndroidEntryPoint
class FavouriteFragment : BaseListFragment<FragmentFavouriteBinding>() {


    override val layoutRes: Int
        get() = R.layout.fragment_favourite

    private val viewModel: FavouriteFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFavouriteList()
        var adapter = initAdapter(FoodAdapter(), binding.itemDisplay, viewModel.favouriteItemList) {
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
