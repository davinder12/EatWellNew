package com.android.eatwell

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EatWell : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this);
        }catch (e:Exception){
            e.printStackTrace()
        }
        AndroidThreeTen.init(this)
    }
}