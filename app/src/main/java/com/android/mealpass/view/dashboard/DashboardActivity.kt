package com.android.mealpass.view.dashboard

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.mealpass.utilitiesclasses.baseclass.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.*
import mealpass.com.mealpass.R


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {


    lateinit var navController: NavController

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


}