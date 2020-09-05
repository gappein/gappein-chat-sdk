package com.gappein.app

import android.app.Application
import com.gappein.sdk.Gappein

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Gappein.Builder().build()


    }

}