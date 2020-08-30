package com.gappein.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.adapter.MessageListAdapter
import kotlinx.android.synthetic.main.activity_message.*


class MessageListActivity : AppCompatActivity() {

    private lateinit var adapter: MessageListAdapter

    companion object {

        private const val CHANNEL_ID = "channelId"
        private const val RECEIVER = "receiver"

        @JvmStatic
        fun buildIntent(context: Context, channelId: String, receiver: User) =
            Intent(context, MessageListActivity::class.java).apply {
                putExtra(CHANNEL_ID, channelId)
                putExtra(RECEIVER, receiver)

            }

        @JvmStatic
        fun buildIntent(context: Context, channelId: String) =
            Intent(context, MessageListActivity::class.java).apply {
                putExtra(CHANNEL_ID, channelId)
            }
    }

    private val channelId by lazy { intent.getStringExtra(CHANNEL_ID) ?: "" }
    private val receiver by lazy { intent.getParcelableExtra(RECEIVER) ?: User() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setupUI()
        setupRecyclerView()
        fetchMessage()
        setupClickListener()
    }

    private fun setupUI() {
        titleToolbar.text = receiver.name
    }

    private fun setupClickListener() {
        buttonSend.setOnClickListener {
            val message = edittext_chatbox.text.toString()
            if (message.isNotEmpty()) {
                ChatClient.instance().sendMessage(message, receiver.token, {
                    edittext_chatbox.text.clear()
                }, {

                })
            }
        }
        imageViewBack.setOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        adapter = MessageListAdapter()
        recyclerViewMessages.layoutManager = LinearLayoutManager(this@MessageListActivity)
        recyclerViewMessages.adapter = adapter

    }

    private fun fetchMessage() {
        ChatClient.instance().getMessages(channelId) {
            adapter.addAll(it)
        }
    }
}