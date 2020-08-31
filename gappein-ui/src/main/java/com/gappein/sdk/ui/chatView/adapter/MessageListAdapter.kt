package com.gappein.sdk.ui.chatView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.chatView.viewholder.ReceiveImageViewHolder
import com.gappein.sdk.ui.chatView.viewholder.ReceiverMessageListViewHolder
import com.gappein.sdk.ui.chatView.viewholder.SenderImageViewHolder
import com.gappein.sdk.ui.chatView.viewholder.SenderMessageListViewHolder

class MessageListAdapter(
    private var messages: ArrayList<Message> = ArrayList<Message>(),
    private val chatClient: ChatClient
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_SENDER_IMAGE = 2
        private const val VIEW_TYPE_RECEIVER = 3
        private const val VIEW_TYPE_RECEIVER_IMAGE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENDER) {
            SenderMessageListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sent_message, parent, false)
            )
        } else if (viewType == VIEW_TYPE_SENDER_IMAGE) {
            SenderImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_sent_message, parent, false)
            )
        } else if (viewType == VIEW_TYPE_RECEIVER_IMAGE) {
            ReceiveImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_received_message, parent, false)
            )
        } else {
            ReceiverMessageListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_received_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENDER) {
            (holder as SenderMessageListViewHolder).bind(messages[position], position)
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVER) {
            (holder as ReceiverMessageListViewHolder).bind(messages[position], position)
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVER_IMAGE) {
            (holder as ReceiveImageViewHolder).bind(messages[position], position)
        } else if (getItemViewType(position) == VIEW_TYPE_SENDER_IMAGE) {
            (holder as SenderImageViewHolder).bind(messages[position], position)
        }
    }

    override fun getItemCount() = messages.count()

    fun addAll(it: List<Message>) {
        messages.run {
            clear()
            addAll(it.reversed())
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUrl) {
            if (chatClient.getUser().token == messages[position].receiver.token) {
                VIEW_TYPE_RECEIVER_IMAGE
            } else {
                VIEW_TYPE_SENDER_IMAGE
            }
        } else {
            if (chatClient.getUser().token == messages[position].receiver.token) {
                VIEW_TYPE_RECEIVER
            } else {
                VIEW_TYPE_SENDER
            }
        }
    }

}