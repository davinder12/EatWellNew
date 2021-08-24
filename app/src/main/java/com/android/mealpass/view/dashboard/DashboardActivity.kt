package com.android.mealpass.view.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.utilitiesclasses.baseclass.BaseActivity
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.viewmodel.DashBoardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.*
import mealpass.com.mealpass.R
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {


    lateinit var navController: NavController

    @Inject
    lateinit var navigationScreen: NavigationScreen


    private val viewModel: DashBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initDrawerBottomSheet()
    }

    private fun initDrawerBottomSheet() {
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_map,
                R.id.navigation_favourite,
                R.id.navigation_setting,
                R.id.navigation_receipt
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //navView.itemIconTintList = null
    }

    fun signOut() {
        bindNetworkState(viewModel.logoutMethod(), progressDialog(R.string.Logout), onError = {
            viewModel.clearData()
            navigationScreen.goToMainScreen(this)

        }) {
            viewModel.clearData()
            navigationScreen.goToMainScreen(this)
        }
    }


}