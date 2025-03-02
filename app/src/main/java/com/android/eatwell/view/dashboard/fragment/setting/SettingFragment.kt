package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.eatwell.data.api.enums.SettingEnum
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.DashboardActivity
import com.android.eatwell.view.dashboard.adapter.SettingAdapter
import com.android.eatwell.view.dashboard.fragment.dialog.DeleteAccountDialog
import com.android.eatwell.view.dashboard.fragment.dialog.SignOutDialog
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.SettingFragmentViewModel
import com.android.eatwell.widgets.alertDialog
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.BuildConfig
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentSettingBinding
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
                SettingEnum.DELETE_ACCOUNT ->{
                    val deleteAccountBottomSheet = DeleteAccountDialog.create {
                        (requireActivity() as? DashboardActivity)?.deleteAccount()
                    }
                    deleteAccountBottomSheet.show(childFragmentManager, deleteAccountBottomSheet.tag)
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