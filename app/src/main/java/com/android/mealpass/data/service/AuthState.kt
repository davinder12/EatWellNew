package com.android.mealpass.data.service

import android.content.Context
import android.content.Intent
import com.android.mealpass.view.login.StartUpActivity
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
        preferenceService.clearPreference()
        val intent = Intent(context, StartUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
