package com.gappein.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gappein.sdk.Gappein
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.view.channelview.ChannelListFragment
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
            ), token = "1234567890", {}, {}
        )

        addFragment()


//        ChatClient.instance().sendMessage(
//            "dskjfndjksnfkjsn",
//            "akaash",
//            {}, {})
//
//        ChatClient.instance().sendMessage(
//            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
//            "akaash",
//            {}, {})
//        ChatClient.instance().openOrCreateChannel("0987654321") {
//
//        }
//        ChatClient.instance().sendMessage(
//            "https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/0.jpeg?alt=media&token=4a2821a4-7c5c-4dc8-91f0-e8cc031dc66d",
//            "0987654321", {
//
//            }, {
//
//            })
//        ChatClient.instance().getMessages() {
//            Log.d("Dsfsdf", it.toString())
//        }
//

//        startActivity(
//            MessageListActivity.buildIntent(this,"[0987654321, 1234567890]",
//            User("0987654321",Date(),"https://firebasestorage.googleapis.com/v0/b/chatsdk-demo-8b9a6.appspot.com/o/images%2F%5B0987654321%2C%201234567890%5D1598906642140.jpg?alt=media&token=772218bb-926b-4217-b9e3-c151085ffd3e","Niharika")
//        ))

    }

    private fun addFragment() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.container,ChannelListFragment.newInstance())
        ft.commit()
    }


}