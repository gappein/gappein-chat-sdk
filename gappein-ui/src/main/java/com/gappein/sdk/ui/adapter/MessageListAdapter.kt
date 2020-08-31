package com.gappein.sdk.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R

class MessageListAdapter(
    private var messages: ArrayList<Message> = ArrayList<Message>(),
    private val chatClient: ChatClient
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENDER) {
            return SenderMessageListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sent_message, parent, false)
            )
        } else {
            return ReceiverMessageListViewHolder(
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
        }
    }

    override fun getItemCount() = messages.count()

    fun addAll(it: List<Message>) {
        messages.run {
            clear()
            addAll(it.reversed())
        }
        notifyItemChanged(it.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatClient.getUser().token == messages[position].receiver.token) {
            VIEW_TYPE_RECEIVER
        } else VIEW_TYPE_SENDER
    }

}