package com.android.mealpass.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.util.Log
import com.android.mealpass.data.extension.checkLocationPermission
import com.android.mealpass.data.extension.getLocationManager
import com.android.mealpass.data.extension.isGPSEnabled
import com.android.mealpass.data.extension.isNetworkEnabled
import com.android.mealpass.data.service.PreferenceService
import dagger.hilt.android.scopes.ViewModelScoped
import mealpass.com.mealpass.R
import java.util.*
import javax.inject.Inject

@ViewModelScoped
class LocationRepository @Inject constructor(
    private val context: Context,
    private val preferenceService: PreferenceService
) {

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 0f // 10 meters
        private const val MIN_TIME_BW_UPDATES = 0L //1000 * 60 * 1 // 1 minute
    }

    @SuppressLint("MissingPermission")
    fun getUserCurrentLocation(response: ((Location?) -> Unit)? = null) {
        val locationManager = context.getLocationManager()
        when {
            context.isNetworkEnabled() -> {
                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    updateLocation(location)
                    response?.invoke(location)
                } else locationManager.updateLocation(LocationManager.NETWORK_PROVIDER) {
                    response?.invoke(it)
                }
            }
            context.isGPSEnabled() -> {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    updateLocation(location)
                    response?.invoke(location)
                } else locationManager.updateLocation(LocationManager.GPS_PROVIDER) {
                    response?.invoke(it)
                }
            }
        }
    }


    private fun LocationManager.updateLocation(
        locationType: String,
        onSuccess: (Location?) -> Unit
    ) {
        if (context.checkLocationPermission()) {
            this.requestLocationUpdates(
                locationType,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        removeUpdates(this)
                        updateLocation(location)
                        onSuccess.invoke(location)
                    }
                })
        }
    }

    //TODO NEED TO REMOVE STATIC LATLONG
    private fun updateLocation(location: Location) {

//    preferenceService.putString(R.string.pkey_userlat, (-27.868406).toString())
//    preferenceService.putString(R.string.pkey_userLong, (153.354059).toString())
        preferenceService.putString(R.string.pkey_userlat, location.latitude.toString())
        preferenceService.putString(R.string.pkey_userLong, location.longitude.toString())

        try {
            val addresses: List<Address> = Geocoder(context, Locale.getDefault()).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            if (!addresses.isNullOrEmpty()) preferenceService.putString(
                R.string.pkey_location,
                addresses[0].countryName
            )
        } catch (exception: Exception) {
            Log.e("country name exception ", exception.message.toString())
        }
    }
}