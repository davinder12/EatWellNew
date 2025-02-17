package com.android.eatwell.view.login.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.eatwell.data.extension.checkNotificationPermission
import com.android.eatwell.utilitiesclasses.baseclass.BaseActivity
import com.android.eatwell.view.login.viewmodel.StartUpActivityViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityStartUpBinding


@AndroidEntryPoint
class StartUpActivity : BaseActivity() {

    lateinit var binding: ActivityStartUpBinding
    private val viewModel: StartUpActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_up)
        setContentView(binding.root)

      //  fireBaseGenerateToken()
        askNotificationPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        fireBaseGenerateToken()
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.navhost_login)

        return navController
            .navigateUp() || super.onSupportNavigateUp()
    }


    private fun fireBaseGenerateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                Log.e("messsage",""+it.result)
                viewModel.updateDeviceToken(it.result)
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (this.checkNotificationPermission()) {
                fireBaseGenerateToken()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


}
