package com.gappein.app

import android.app.Application
import com.gappein.app.data.AppPreference
import com.gappein.sdk.Gappein

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreference.init(this)
        // Pass the Giphy API key along with context to initialize the Giphy SDK,
        // like so -> Gappein.initialize(this, "API KEY here")
        // OR
        // Don't pass any parameters, if you don't want to use Giphy.
        Gappein.initialize()
    }

}