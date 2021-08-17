package com.android.mealpass.view.login

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.android.mealpass.utilitiesclasses.baseclass.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_start_up.*
import mealpass.com.mealpass.R

@AndroidEntryPoint
class StartUpActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)
        //viewModel.updateDeviceToken()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navhost_login.findNavController()
            .navigateUp() || super.onSupportNavigateUp()
    }


    private fun fireBaseGenerateToken() {

//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            viewModel.updateDeviceToken(it.token)
//        }token
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            for (fragment in supportFragmentManager.fragments) {
//                fragment.onActivityResult(requestCode, resultCode, data)
//            }
//        } catch (e: Exception) {
//            Log.d("ERROR", e.toString())
//        }
//    }


}
