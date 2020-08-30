package com.gappein.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
        Glide.with(this)
            .load("https://in.bmscdn.com/iedb/artist/images/website/poster/large/shah-rukh-khan-2092-12-09-2017-02-10-43.jpg")
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(32)))
            .into(avatarImageView)
    }

    private fun setupClickListener() {
        buttonSend.setOnClickListener {
            val message = edittext_chatbox.text.toString()
            if (message.isNotEmpty()) {
                ChatClient.getInstance().sendMessage(message, receiver.token, {
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
        ChatClient.getInstance().getMessages(channelId) {
            adapter.addAll(it)
        }
    }
}