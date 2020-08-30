package com.gappein.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gappein.sdk.Gappein
import com.gappein.sdk.model.User
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Gappein.Builder().build()

        Gappein.getInstance().setUser(
            User(
                token = "sdfsdfsd",
                createdAt = Date(),
                profileImageUrl = "Sdfsdfsdf",
                name = "Himanshu"
            ), token = "sdfsdfsd"
        )

    }
}