package com.android.mealpass.view.dashboard.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.mealpass.data.extension.isGPSEnabled
import com.android.mealpass.data.extension.onTextChange
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.adapter.FoodAdapter
import com.android.mealpass.view.dashboard.fragment.dialog.FoodFilter
import com.android.mealpass.view.dashboard.viewmodel.FindFoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentHomeBinding
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

    @Inject
    lateinit var navigation: NavigationScreen

    private val viewModel: FindFoodViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(binding.filterBtn.throttleClicks()) {
            val resturantFilter = FoodFilter.create {
                searchItem(binding.searchText.text.toString())
            }
            resturantFilter.show(childFragmentManager, resturantFilter.tag)
        }

        var adapter =
            initAdapter(FoodAdapter(), binding.resturantList, viewModel.foodResource) { items ->
                navigation.productDetailScreen(items.id, items.storename)
            }

        subscribe(binding.searchText.onTextChange(1)) {
            searchItem(it.toString())
        }

        subscribe(binding.notificaiton.throttleClicks()) {

        }

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


}

