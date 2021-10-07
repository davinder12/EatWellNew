package com.android.mealpass.view.dashboard.fragment.setting

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import com.android.mealpass.data.extension.progressDialog
import com.android.mealpass.data.extension.throttleClicks
import com.android.mealpass.utilitiesclasses.baseclass.DataBindingActivity
import com.android.mealpass.view.dashboard.fragment.setting.viewmodel.ProfileActivityViewModel
import com.android.mealpass.view.units.tryHandleCropImageResult
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.resolution
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.ActivityProfileBinding


@AndroidEntryPoint
class ProfileActivity : DataBindingActivity<ActivityProfileBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_profile


    companion object {
        const val ITEM_SELECTED = "1"
        const val ITEM_NOT_SELECTED = "0"
        private const val PROFILE_IMAGE_SIZE = 300

    }

    private val viewModel : ProfileActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileData()
        bindNetworkState(viewModel.networkState, progressDialog(R.string.Pleasewait),onError = {
            finish()
        })



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




        subscribe(profilePick.throttleClicks()){
            callImagePicker()

        }



        subscribe(edit_icon.throttleClicks()){
            viewModel. updateEditableValue()
        }
    }

    private fun callImagePicker(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropMenuCropButtonTitle(getString(R.string.Continue))
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tryHandleCropImageResult(requestCode, resultCode, data) {
            lifecycleScope.launch {
                val compressedImageFile = Compressor.compress(this@ProfileActivity, it.uri.toFile()) {
                    resolution(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE)
                    format(Bitmap.CompressFormat.PNG)
                }
                viewModel.updateProfilePic(compressedImageFile.absolutePath)


            }
        }
    }

    override fun onBindView(binding: ActivityProfileBinding) {
        binding.vm =viewModel
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }
}
