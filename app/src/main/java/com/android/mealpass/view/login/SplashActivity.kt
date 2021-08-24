package com.android.mealpass.view.login

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.android.mealpass.data.service.PreferenceService
import com.android.mealpass.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    // TODO Auto-generated method stub

    @Inject
    lateinit var prefference: PreferenceService

    @Inject
    lateinit var navigationScreen: NavigationScreen

    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigateScreenAfterDelay()


    }

    private fun navigateScreenAfterDelay() {
        Handler().postDelayed({
              when {
                  !prefference.getString(R.string.pkey_user_Id).isNullOrEmpty() && prefference.getBoolean(R.string.pkey_isMerchantLogin) -> {
                      navigationScreen.goToMerchantScreen() }
                  !prefference.getString(R.string.pkey_user_Id).isNullOrEmpty() -> navigationScreen.goToDashBoard()
                  else -> navigationScreen.goToStartUpScreen()

              }
        }, SPLASH_TIME_OUT)
    }


}
