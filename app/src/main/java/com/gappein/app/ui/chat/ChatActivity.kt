package com.gappein.app.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gappein.app.R
import com.gappein.sdk.ui.view.channelview.ChannelListFragment

class ChatActivity : AppCompatActivity() {

    companion object {

        private const val LIST_ALL_CHANNELS = "list_all_channels"

        fun buildIntent(context: Context)= Intent(context, ChatActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ChannelListFragment.newInstance(), ChannelListFragment.TAG)
            .commit()
    }
}