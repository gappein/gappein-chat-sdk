package com.gappein.sdk.ui.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.util.DatesUtil
import kotlinx.android.synthetic.main.item_received_message.view.*

class ReceiverMessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(message: Message, position: Int) {
        view.receivedTextMessage.text = message.message
        view.receivedTextMessageTime.text = DatesUtil.getTimeAgo(message.timeStamp)
    }
}