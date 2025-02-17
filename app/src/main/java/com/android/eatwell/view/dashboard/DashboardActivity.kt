package com.android.eatwell.view.dashboard

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.utilitiesclasses.baseclass.BaseActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.viewmodel.DashBoardViewModel
import com.android.eatwell.view.units.AlarmReceiver
import com.android.eatwell.widgets.alertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityDashboardBinding
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    lateinit var binding : ActivityDashboardBinding
    lateinit var navController: NavController

    @Inject
    lateinit var navigationScreen: NavigationScreen

    companion object{
        const val url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        const val VERSION_MESSAGE = "Application is UpToDate"
    }

     val viewModel: DashBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDrawerBottomSheet()



        // pushNotificationHandling()
        // checkUpdate()
        viewModel.isLocalNotificationInit.observe(this) {
            if (!it) {
                Log.e("alarm receiver inital", "started")
                alarmManager()
            }
        }


        bindNetworkState(viewModel.versionNetworkState, progressDialog(R.string.Pleasewait)) {
            val message = resources.getString(R.string.VersionMsg, viewModel.updatedVersion)
            if (viewModel.needToUpdateVersion) {
                viewModel.needToUpdateVersion = false
                alertDialog(getString(R.string.app_name),
                        message, getString(R.string.Continue), getString(R.string.Cancel
                ), successResponse = {
                    navigationScreen.goToSocialPage(url)
                })
            } else {
                showMessage(getString(R.string.updateVersion))
            }
        }
    }


    private fun alarmManager() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager?.canScheduleExactAlarms() == true) {
                alarmManagerFun(alarmManager)
            } else {
                // val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                // intent.data = Uri.parse("package:$packageName")
              //  requestPermissionLauncher.launch(intent)
                alarmManagerFun(alarmManager)
            }
        } else {
            alarmManagerFun(alarmManager)
        }
    }

    private fun alarmManagerFun(alarmManager: AlarmManager?) {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 16)
        }
        val alarmIntent: PendingIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0 or FLAG_IMMUTABLE)
        }

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        viewModel.updateAlarmManagerStatus()
    }


    private fun checkUpdate() {
//        val appUpdateInfoTask: Task<AppUpdateInfo> = AppUpdateManagerFactory.create(this).appUpdateInfo
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
//                            AppUpdateType.FLEXIBLE)) {
//                Log.e("information", "" + appUpdateInfo.availableVersionCode())
//            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                Log.e("sdf", "sdfdsf")
//
//                //  popupSnackBarForCompleteUpdate()
//            }
//        }
//
//        appUpdateInfoTask.addOnFailureListener {
//            Log.e("response", "" + it.message)
//        }
    }


//    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
//        try {
//            appUpdateManager.startUpdateFlowForResult(
//                appUpdateInfo,
//                AppUpdateType.FLEXIBLE,
//                this,
//                Flexible.FLEXIBLE_APP_UPDATE_REQ_CODE
//            )
//        } catch (e: SendIntentException) {
//            e.printStackTrace()
//        }
//    }

    private fun initDrawerBottomSheet() {
        setSupportActionBar(binding.toolbar)
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
        bindNetworkState(viewModel.logoutMethod(), progressDialog(R.string.Pleasewait), onError = {
            NotificationManagerCompat.from(this).cancelAll()
            viewModel.clearData()
            navigationScreen.goToMainScreen(this)

        }) {
            viewModel.clearData()
            NotificationManagerCompat.from(this).cancelAll()
            navigationScreen.goToMainScreen(this)
        }
    }

    fun deleteAccount() {
        bindNetworkState(viewModel.deleteMethod(), progressDialog(R.string.Pleasewait), onError = {
        }) {
            viewModel.clearData()
            NotificationManagerCompat.from(this).cancelAll()
            navigationScreen.goToMainScreen(this)
        }
    }


}