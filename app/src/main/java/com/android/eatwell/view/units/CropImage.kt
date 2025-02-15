package com.android.eatwell.view.units

import android.content.Intent
import com.canhub.cropper.CropImage

fun tryHandleCropImageResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    onSuccess: (CropImage.ActivityResult) -> Unit
): Boolean {
//    if (requestCode != CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) return false
//    val result = CropImage.getActivityResult(data)
//    if (resultCode == Activity.RESULT_OK) {
//        onSuccess(result)
//    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//        //Timber.e(result.error)
//    }
    return true
}