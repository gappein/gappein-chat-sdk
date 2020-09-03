package com.gappein.sdk.ui.view.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.item_received_message.view.*

class ReceiverMessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(messages: ArrayList<Message>, position: Int) {
        messages.forEachIndexed { index, content ->
            val prevAuthor = messages.getOrNull(position - 1)?.sender
            val nextAuthor = messages.getOrNull(position + 1)?.sender
            val isFirstMessageByAuthor = prevAuthor != content.sender
            val isLastMessageByAuthor = nextAuthor != content.sender

            if (isFirstMessageByAuthor) {
                if (index == 0) {
                    view.receivedTextMessageTime.text = DatesUtil.getTimeAgo(content.timeStamp)
                    view.receivedTextMessageTime.show()
                } else view.receivedTextMessageTime.hide()
            }
        }
            view.receivedTextMessage.text =  messages[position].message
    }
}