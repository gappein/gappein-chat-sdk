package com.gappein.app

import android.app.Application
import com.gappein.app.data.AppPreference
import com.gappein.sdk.Gappein

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreference.init(this)
        // Pass the Giphy key to initialize the Giphy SDK to send gifs!
        Gappein.initialize(this, "GIPHY_KEY_HERE")
    }

}