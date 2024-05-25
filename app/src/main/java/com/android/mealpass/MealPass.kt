package com.android.mealpass

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MealPass : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this);
        AndroidThreeTen.init(this)
    }
}