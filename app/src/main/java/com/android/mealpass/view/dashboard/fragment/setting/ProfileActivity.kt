package com.android.mealpass.view.dashboard.fragment.setting

import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ProfileActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityProfileBinding


@AndroidEntryPoint
class ProfileActivity : DataBindingActivity<ActivityProfileBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_profile

    companion object {
        const val CAMERA_RESULT_CODE = 101
        const val ITEM_SELECTED = "1"
        const val ITEM_NOT_SELECTED = "0"

    }

    private val viewModel : ProfileActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileData()
        bindNetworkState(
            viewModel.networkState,
            progressDialog(R.string.Pleasewait)
        )


        subscribe(binding.update.throttleClicks()){
            viewModel.filterMethod{status,message->
                when{
                    status -> bindNetworkState(
                        viewModel.uploadProfileData(newslatter.isChecked), progressDialog(R.string.Pleasewait),
                        success = R.string.ProfileUpdated
                    ) {
                        finish()
                    }
                    else -> showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                }
            }
        }

        subscribe(menu_layout.throttleClicks()){
            finish()
        }


        val launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH)?.let { imagePath ->
                        viewModel.updateProfilePic(imagePath)
                    }

            }
        }

        subscribe(profilePick.throttleClicks()){
            runWithPermissions(Permission.CAMERA) {
                val intent = Intent(this, ImageSelectActivity::class.java)
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true)//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)//default is true
                launchSomeActivity.launch(intent)

               // startActivityForResult(intent, CAMERA_RESULT_CODE)
            }
        }
        subscribe(edit_icon.throttleClicks()){
            viewModel. updateEditableValue()
        }
    }


    override fun onBindView(binding: ActivityProfileBinding) {
        binding.vm =viewModel
    }
}
