package com.android.mealpass.data.service

import android.content.Context
import android.content.Intent
import com.android.mealpass.view.login.StartUpActivity
import mealpass.com.mealpass.R
import javax.inject.Inject

/**
 * Authentication state.
 * Separate class to avoid cyclic dependency with OkHttpClient.
 */
data class AuthState @Inject constructor(
    private val context: Context,
    private val preferenceService: PreferenceService
) {
    fun logout() {
        clearData()
        val intent = Intent(context, StartUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    private fun clearData() {
        preferenceService.putString(R.string.pkey_user_Id, "")
        preferenceService.putBoolean(R.string.pkey_social_login,false)
        preferenceService.putString(R.string.pkey_secure_token, "")
        preferenceService.putString(R.string.pkey_social_emaiId, "")
        preferenceService.putString(R.string.pkey_phoneNumber, "")
        // preferenceService.clearPreference()
    }
}
