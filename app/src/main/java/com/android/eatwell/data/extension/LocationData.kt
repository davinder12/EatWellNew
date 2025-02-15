package com.android.eatwell.data.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


const val DEFAULT_SECOND = 2L


fun EditText.onTextChange(ms: Long = DEFAULT_SECOND): Observable<CharSequence>? {
    return this.textChanges().debounce(ms, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
}

fun Context.isGPSEnabled() =
    (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
        LocationManager.GPS_PROVIDER
    )

fun Context.checkNotificationPermission(): Boolean = (ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.POST_NOTIFICATIONS
) == PackageManager.PERMISSION_GRANTED)


fun Context.isNetworkEnabled() =
    (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )

fun Context.getLocationManager() = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)

fun Context.checkLocationPermission(): Boolean =
    this.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && this.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


fun Context.Permission(): Boolean = ActivityCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_COARSE_LOCATION
) != PackageManager.PERMISSION_GRANTED


