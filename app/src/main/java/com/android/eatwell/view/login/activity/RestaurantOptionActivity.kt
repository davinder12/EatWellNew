package com.android.eatwell.view.login.activity

import android.os.Bundle
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentResturantOptionBinding
import javax.inject.Inject


@AndroidEntryPoint
class RestaurantOptionActivity : DataBindingActivity<FragmentResturantOptionBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_resturant_option

    @Inject
    lateinit var navigationScreen: NavigationScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(binding.resturnatPickupBtn.throttleClicks()) {
            navigationScreen.goToDashBoard()
            finish()
        }

        subscribe(binding.restaurantClaimBtn.throttleClicks()) {
            navigationScreen.goToDashBoard()
            finish()
        }

        subscribe(binding.resturantEnjoyMealBtn.throttleClicks()) {
            navigationScreen.goToDashBoard()
            finish()
        }

        subscribe(binding.nextBtn.throttleClicks()) {
            navigationScreen.goToDashBoard()
            finish()
        }

    }

    override fun onBackPressed() {}


}
