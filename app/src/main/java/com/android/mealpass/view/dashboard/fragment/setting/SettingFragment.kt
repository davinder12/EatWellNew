package com.android.mealpass.view.dashboard.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mealpass.data.api.enums.SettingEnum
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.common.NavigationScreen
import com.android.mealpass.view.dashboard.DashboardActivity
import com.android.mealpass.view.dashboard.adapter.SettingAdapter
import com.android.mealpass.view.dashboard.fragment.dialog.SignOutDialog
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.SettingFragmentViewModel
import com.android.mealpass.widgets.alertDialog
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentSettingBinding
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseListFragment<FragmentSettingBinding>() {


    private val viewModel: SettingFragmentViewModel by viewModels()


    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.fragment_setting




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = initAdapter(SettingAdapter(viewModel.isLocationNotification, BuildConfig.VERSION_NAME), binding.settingList, viewModel.settingList)
        {
            when (it) {
                SettingEnum.ADD_REFERRAL_CODE -> navigationScreen.goToReferralCodeScreen()
                SettingEnum.ADD_STAFF_CODE -> navigationScreen.goToStaffCodeScreen()
                SettingEnum.PROFILE -> navigationScreen.gotToProfileActivity()
                SettingEnum.CHANGE_PASSWORD -> navigationScreen.gotToChangePasswordActivity()
                SettingEnum.NOTIFICATION -> {
                }
                SettingEnum.BUILD_VERSION -> {
                    (requireActivity() as DashboardActivity).viewModel.checkVersionOnPlayStore()
                }
                SettingEnum.CONTACT_US_FEEDBACK -> navigationScreen.goToFeedBackScreen()
                else -> {
                    val signOutBottomSheet = SignOutDialog.create {
                        (requireActivity() as? DashboardActivity)?.signOut()
                    }
                    signOutBottomSheet.show(childFragmentManager, signOutBottomSheet.tag)
                }
            }
      }

        subscribe(adapter.itemClicks){
            if(viewModel.isLocationNotification.value == true){
                requireContext().alertDialog(resources.getString(R.string.Notification),
                        resources.getString(R.string.SwitchOffDailyPushAlert)+"\n"+ resources.getString(R.string.SwitchOffDailyPushNote),
                        getString(R.string.Continue),
                        getString(R.string.Cancel),
                        successResponse = {
                            viewModel.updateNotification()
                            adapter.notifyItemChanged(4)
                        })
            } else {
                viewModel.updateNotification()
                adapter.notifyItemChanged(4)
            }
        }
    }





}