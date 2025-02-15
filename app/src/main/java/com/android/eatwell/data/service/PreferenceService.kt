package com.android.eatwell.data.service

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

/**
 * Preference service
 *
 */
class PreferenceService @Inject constructor(context: Context) {

    // private val defaultSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources
    //  val masterKeyAlias = //MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val defaultSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "MealPass",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    operator fun contains(@StringRes resId: Int): Boolean {
        return defaultSharedPreferences.contains(resources.getString(resId))
    }

    fun remove(@StringRes resId: Int) {
        defaultSharedPreferences.edit().remove(resources.getString(resId)).apply()
    }


    fun getString(@StringRes resId: Int, default: String? = ""): String? {
        return defaultSharedPreferences.getString(resources.getString(resId), default)
    }

    fun putString(@StringRes resId: Int, value: String? = "") {
        defaultSharedPreferences.edit().putString(resources.getString(resId), value).apply()
    }

    fun getInt(@StringRes resId: Int, default: Int = 0): Int {
        return defaultSharedPreferences.getInt(resources.getString(resId), default)
    }

    fun putInt(@StringRes resId: Int, value: Int) {
        defaultSharedPreferences.edit().putInt(resources.getString(resId), value).apply()
    }

    fun putBoolean(@StringRes resId: Int, value: Boolean) {
        defaultSharedPreferences.edit().putBoolean(resources.getString(resId), value).apply()
    }

    fun getBoolean(@StringRes resId: Int, default: Boolean = false): Boolean {
        return defaultSharedPreferences.getBoolean(resources.getString(resId), default)
    }


    fun putBoolean(resId: String, value: Boolean) {
        defaultSharedPreferences.edit().putBoolean(resId, value).apply()
    }

    fun getBoolean(resId: String, default: Boolean = false): Boolean {
        return defaultSharedPreferences.getBoolean(resId, default)
    }

    fun getString( resId: String, default: String? = ""): String? {
        return defaultSharedPreferences.getString(resId, default)
    }

    fun putString( resId: String, value: String? = "") {
        defaultSharedPreferences.edit().putString(resId, value).apply()
    }


    fun putLong(@StringRes resId: Int, value: Long) {
        putLong(resources.getString(resId), value)
    }

    fun putLong(key: String, value: Long) {
        defaultSharedPreferences.edit().putLong(key, value).apply()
    }

    fun getLong(@StringRes resId: Int): Long {
        return getLong(resources.getString(resId))
    }

    fun getLong(key: String, default: Long = 0): Long {
        return defaultSharedPreferences.getLong(key, default)
    }


    fun getFloat(@StringRes resId: Int, default: Float = 0f): Float {
        return defaultSharedPreferences.getFloat(resources.getString(resId), default)
    }

    fun putFloat(@StringRes resId: Int, value: Float) {
        defaultSharedPreferences.edit().putFloat(resources.getString(resId), value).apply()
    }


    fun clearPreference() {
        defaultSharedPreferences.edit().clear().apply()
    }

}
