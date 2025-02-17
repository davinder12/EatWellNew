package com.android.eatwell.view.dashboard.fragment.setting

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import com.android.eatwell.data.extension.progressDialog
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.utilitiesclasses.baseclass.DataBindingActivity
import com.android.eatwell.view.dashboard.fragment.setting.viewmodel.ProfileActivityViewModel
import com.canhub.cropper.*
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.ActivityProfileBinding
import java.io.File


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
                        viewModel.uploadProfileData(binding.newslatter.isChecked), progressDialog(R.string.Pleasewait),
                        success = R.string.ProfileUpdated
                    ) {
                        finish()
                    }
                    else -> showSnackMessage(resources.getString(message ?: R.string.Unknown_msg))
                }
            }
        }

        subscribe(binding.menuLayout.throttleClicks()){
            finish()
        }




        subscribe(binding.profilePick.throttleClicks()){
            callImagePicker()

        }



        subscribe(binding.editIcon.throttleClicks()){
            viewModel. updateEditableValue()
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            val croppedImageUri = result.uriContent
            val croppedImageFilePath = result.getUriFilePath(this) // optional usage
            // Process the cropped image URI as needed.
            croppedImageUri?.let {
                getTempFileFromUri(croppedImageUri).let { path ->
                    lifecycleScope.launch {
                        path?.let {
                        val compressedImageFile = Compressor.compress(this@ProfileActivity, it) {
                            resolution(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE)
                            format(Bitmap.CompressFormat.PNG)
                        }
                        viewModel.updateProfilePic(compressedImageFile.absolutePath)
                    }
                    }
                }
            }

        } else {
            // An error occurred.
            val exception = result.error
            // Handle the error.
        }
    }



    private fun getTempFileFromUri(uri: Uri): File? {
        val documentFile = DocumentFile.fromSingleUri(this, uri)
        return if (documentFile != null && documentFile.isFile) {
            val fileName = documentFile.name ?: "temp_image.png"
            val tempFile = File(cacheDir, fileName)

            try {
                contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                tempFile // Return the file if successfully created
            } catch (e: Exception) {
                e.printStackTrace()
                null // Return null in case of an error
            }
        } else {
            null // Return null if the URI does not resolve to a valid file
        }
    }




    private fun callImagePicker(){
//        cropImage.launch(
//            CropImageContractOptions(
//                uri = null,
//                cropImageOptions = CropImageOptions(
//                    guidelines = CropImageView.Guidelines.ON,
//                    cropMenuCropButtonTitle = getString(R.string.Continue),
//                    cropShape = CropImageView.CropShape.OVAL,
//                    aspectRatioX = 1,
//                    aspectRatioY = 1
//                )
//            )
//        )

        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()

//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setCropMenuCropButtonTitle(getString(R.string.Continue))
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setAspectRatio(1, 1)
//                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            //Image Uri will not be null for RESULT_OK
            val uri: Uri? = data?.data
        uri?.let { uri->
            lifecycleScope.launch {
                val compressedImageFile = Compressor.compress(this@ProfileActivity, uri.toFile()) {
                    resolution(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE)
                    format(Bitmap.CompressFormat.PNG)
                }
                viewModel.updateProfilePic(compressedImageFile.absolutePath)
            }
        }
            }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        tryHandleCropImageResult(requestCode, resultCode, data) {
//            lifecycleScope.launch {
//                val compressedImageFile = Compressor.compress(this@ProfileActivity, it.uri.toFile()) {
//                    resolution(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE)
//                    format(Bitmap.CompressFormat.PNG)
//                }
//                viewModel.updateProfilePic(compressedImageFile.absolutePath)
//            }
//        }
//    }

    override fun onBindView(binding: ActivityProfileBinding) {
        binding.vm =viewModel
    }

    override fun onPause() {
        super.onPause()
        hideKeboard()
    }
}
