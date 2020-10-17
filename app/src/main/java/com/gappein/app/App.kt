package com.gappein.app

import android.app.Application
import com.gappein.app.data.AppPreference
import com.gappein.sdk.Gappein

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreference.init(this)
        Gappein.initialize(this)
    }

}