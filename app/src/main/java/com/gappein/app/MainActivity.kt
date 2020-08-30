package com.gappein.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gappein.sdk.Gappein
import com.gappein.sdk.client.ChatClient
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
        Gappein.getInstance().setUser(
            User(
                token = "qpppq",
                createdAt = Date(),
                profileImageUrl = "Sdfsdfsdf",
                name = "Himanshu"
            ), token = "sdfsdfsd"
        )
        Gappein.getInstance().setUser(
            User(
                token = "01010101",
                createdAt = Date(),
                profileImageUrl = "001",
                name = "Himanshu000"
            ), token = "sdfsdfs000d"
        )
        Gappein.getInstance().setUser(
            User(
                token = "1111",
                createdAt = Date(),
                profileImageUrl = "1111",
                name = "1111"
            ), token = "111"
        )

        ChatClient.instance().sendMessage(
            "dskjfndjksnfkjsn",
            "akaash",
            {}, {})
        ChatClient.instance().sendMessage(
            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
            "Himanshu",
            {}, {})
        ChatClient.instance().sendMessage(
            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
            "akaash",
            {}, {})
        ChatClient.instance().openOrCreateChannel("jghvhvhgv", {
            Log.d("DSfsdf", it)
        })
        ChatClient.instance().getAllChannels {

        }

    }
}