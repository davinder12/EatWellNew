package com.android.mealpass.view.login.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mealpass.utilitiesclasses.baseclass.BaseActivity
import com.android.mealpass.view.login.viewmodel.StartUpActivityViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_start_up.*
import mealpass.com.mealpass.R


@AndroidEntryPoint
class StartUpActivity : BaseActivity() {

    private val viewModel: StartUpActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)
        fireBaseGenerateToken()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navhost_login.findNavController()
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


}
