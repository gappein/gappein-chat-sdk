package com.gappein.app

import android.os.Bundle
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
                token = "1234567890",
                createdAt = Date(),
                profileImageUrl = "1234567890",
                name = "Himanshu"
            ), token = "1234567890"
        )


//        ChatClient.instance().sendMessage(
//            "dskjfndjksnfkjsn",
//            "akaash",
//            {}, {})
//
//        ChatClient.instance().sendMessage(
//            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
//            "akaash",
//            {}, {})
        ChatClient.instance().openOrCreateChannel("0987654321") {

        }
        ChatClient.instance().sendMessage(
            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
            "0987654321",
            {
            }, {

            })
//        ChatClient.instance().getAllChannels {
//
//        }

    }
}