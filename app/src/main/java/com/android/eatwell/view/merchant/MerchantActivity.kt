package com.android.eatwell.view.merchant

import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityMerchantBinding
import eatwell.com.eatwell.databinding.ActivityStartUpBinding

@AndroidEntryPoint
class MerchantActivity : DataBindingActivity<ActivityMerchantBinding>() {


    override val layoutRes: Int
        get() = R.layout.activity_merchant


    override fun onSupportNavigateUp(): Boolean {

       val navController = findNavController(R.id.nav_merchant_host)

        return navController
            .navigateUp() || super.onSupportNavigateUp()
    }




//    fun clearActivity() {
//        NavigationScreen(this).goToMainScreen(this)
//    }


}