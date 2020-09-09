package com.gappein.app.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gappein.app.R
import com.gappein.sdk.ui.view.channelview.ChannelListFragment

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ChannelListFragment.newInstance(), ChannelListFragment.TAG)
            .disallowAddToBackStack()
            .commit()
    }
}