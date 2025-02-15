package com.android.eatwell.view.login.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.android.eatwell.data.service.PreferenceService
import com.android.eatwell.view.common.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
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
        //throw RuntimeException("Test Crash") // Force a crash

    }

    private fun navigateScreenAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                !prefference.getString(R.string.pkey_user_Id).isNullOrEmpty() && prefference.getBoolean(R.string.pkey_isMerchantLogin) -> {
                    navigationScreen.goToMerchantScreen() }
                !prefference.getString(R.string.pkey_user_Id).isNullOrEmpty() -> navigationScreen.goToDashBoard()
                else -> navigationScreen.goToStartUpScreen()
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}
