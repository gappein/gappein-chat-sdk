package com.gappein.app

import android.app.Application
import com.gappein.app.data.AppPreference
import com.gappein.sdk.Gappein

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreference.init(this)
        // In Gappein.initialize()
        // Pass the Giphy API key along with context to initialize the Giphy SDK.
        // OR
        // Don't pass any parameters, if you don't want to use Giphy.
        Gappein.initialize()
    }

}