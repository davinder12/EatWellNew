package com.android.eatwell.view.merchant

import androidx.navigation.fragment.findNavController
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_merchant.*
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityMerchantBinding

@AndroidEntryPoint
class MerchantActivity : DataBindingActivity<ActivityMerchantBinding>() {


    override val layoutRes: Int
        get() = R.layout.activity_merchant


    override fun onSupportNavigateUp(): Boolean {
        return nav_merchant_host.findNavController()
            .navigateUp() || super.onSupportNavigateUp()
    }




//    fun clearActivity() {
//        NavigationScreen(this).goToMainScreen(this)
//    }


}