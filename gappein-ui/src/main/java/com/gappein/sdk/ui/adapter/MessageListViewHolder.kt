package com.gappein.sdk.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import kotlinx.android.synthetic.main.item_sent_message.view.*

class MessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(message: Message, position: Int) {
        view.sentTextMessage.text = message.message
    }
}