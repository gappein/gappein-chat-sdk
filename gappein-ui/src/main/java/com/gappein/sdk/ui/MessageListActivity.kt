package com.gappein.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MessageListActivity : AppCompatActivity() {

    companion object {

        private const val CHANNEL_ID = "channelId"

        @JvmStatic
        fun buildIntent(context: Context, channelId: String) =
            Intent(context, MessageListActivity::class.java).apply {
                putExtra(CHANNEL_ID, channelId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }
}