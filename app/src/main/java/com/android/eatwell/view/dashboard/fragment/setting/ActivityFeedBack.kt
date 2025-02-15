package com.android.eatwell.view.dashboard.fragment.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.FeedBackViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityFeedbackBinding
import javax.inject.Inject

@AndroidEntryPoint
class ActivityFeedBack : DataBindingActivity<ActivityFeedbackBinding>() {


    private val viewModel: FeedBackViewModel by viewModels()

    @Inject
    lateinit var navigationScreen: NavigationScreen

    override val layoutRes: Int
        get() = R.layout.activity_feedback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        subscribe(binding.cross.throttleClicks()){
            finish()
        }

        subscribe(binding.sendFeebackBtn.throttleClicks()){
            viewModel.filterMethod { status, message ->
                when {
                    status -> bindNetworkState(
                            viewModel.updateFeedBack(),
                            progressDialog(R.string.Pleasewait),
                            R.string.FeedbackThanksMessage
                    ) {
                        finish()
                    }
                    else -> showSnackMessage(resources.getString(message?:R.string.Unknown_msg))
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initAdapter(SettingAdapter(),binding.settingList,viewModel.settingList){
//            when(it){
//                SettingEnum.ADD_REFERRAL_CODE -> navigationScreen.goToReferralCodeScreen()
//                SettingEnum.PROFILE -> navigationScreen.gotToProfileActivity()
//                SettingEnum.CHANGE_PASSWORD -> navigationScreen.gotToChangePasswordActivity()
//                SettingEnum.NOTIFICATION -> navigationScreen.gotToChangePasswordActivity()
//                SettingEnum.CONTACT_US_FEEDBACK -> navigationScreen.gotToChangePasswordActivity()
//                else-> {
//                    val signOutBottomSheet = SignOutDialog.create {
//                        (requireActivity() as? DashboardActivity)?.signOut()
//                    }
//                    signOutBottomSheet.show(childFragmentManager, signOutBottomSheet.tag)
//                }
//            }
//        }
//    }

    override fun onBindView(binding: ActivityFeedbackBinding) {
        binding.vm = viewModel
    }
}