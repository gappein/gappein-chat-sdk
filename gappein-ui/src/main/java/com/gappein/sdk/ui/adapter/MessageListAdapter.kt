package com.gappein.sdk.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R

class MessageListAdapter(private var messages: ArrayList<Message> = ArrayList<Message>()) :
    RecyclerView.Adapter<MessageListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
        return MessageListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        holder.bind(messages[position], position)
    }

    override fun getItemCount() = messages.count()

    fun addAll(it: List<Message>) {
        messages.addAll(it)
        notifyDataSetChanged()
    }

}