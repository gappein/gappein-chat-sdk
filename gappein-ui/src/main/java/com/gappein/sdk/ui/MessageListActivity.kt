package com.gappein.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.adapter.MessageListAdapter
import kotlinx.android.synthetic.main.activity_message.*


class MessageListActivity : AppCompatActivity() {

    private lateinit var adapter: MessageListAdapter

    companion object {

        private const val CHANNEL_ID = "channelId"

        @JvmStatic
        fun buildIntent(context: Context, channelId: String) =
            Intent(context, MessageListActivity::class.java).apply {
                putExtra(CHANNEL_ID, channelId)
            }
    }

    private val channelId by lazy { intent.getStringExtra(CHANNEL_ID) ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setupRecyclerView()
        setupMessages()
    }

    private fun setupRecyclerView() {
        adapter = MessageListAdapter()
        recyclerViewMessages.setLayoutManager(LinearLayoutManager(this))
        recyclerViewMessages.setAdapter(adapter)
    }

    private fun setupMessages() {
        ChatClient.instance().getMessages(channelId) {
            adapter.addAll(it)
        }
    }
}