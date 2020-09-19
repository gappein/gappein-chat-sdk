package com.gappein.sdk.ui.view.chatView.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.item_received_message.view.*
import kotlinx.android.synthetic.main.item_sent_message.view.*

class ReceiverMessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(messages: List<Message>, position: Int) {
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
        if (messages[position].deleted) {
            view.receivedTextMessage.text = view.context.getString(R.string.delete_by_user)
            view.receivedTextMessage.setTypeface(  view.receivedTextMessage.typeface, Typeface.ITALIC)
            view.receivedTextMessage.setTextColor(Color.parseColor("#828282"))
            view.receivedTextMessage.setBackgroundResource(R.drawable.recieved_message_deleted_background)
        } else {
            view.receivedTextMessage.text = (messages[position].message)
        }
    }
}