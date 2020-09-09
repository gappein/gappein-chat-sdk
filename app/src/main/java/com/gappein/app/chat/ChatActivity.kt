package com.gappein.app.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gappein.app.R
import com.gappein.sdk.ui.view.channelview.ChannelListFragment

class ChatActivity : AppCompatActivity() {

    companion object {

        private const val LIST_ALL_CHANNELS = "list_all_channels"

        fun buildIntent(context: Context, listAllChannel: Boolean): Intent {
            return Intent(context, ChatActivity::class.java).putExtra(
                LIST_ALL_CHANNELS,
                listAllChannel
            )
        }
    }

    private val listAllChannel by lazy {intent.getBooleanExtra(LIST_ALL_CHANNELS,false)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ChannelListFragment.newInstance(listAllChannel), ChannelListFragment.TAG)
            .commit()
    }
}