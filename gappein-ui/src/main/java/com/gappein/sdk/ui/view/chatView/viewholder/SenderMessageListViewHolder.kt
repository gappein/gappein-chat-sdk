package com.gappein.sdk.ui.view.chatView.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.item_sent_message.view.*

class SenderMessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(position: Int, messages: List<Message>, onMessageClick: (String) -> Unit) {

        messages.forEachIndexed { index, content ->
            val prevAuthor = messages.getOrNull(position.plus(1))?.sender
            val nextAuthor = messages.getOrNull(position.minus(1))?.sender
            val isFirstMessageByAuthor = prevAuthor != content.sender
            val isLastMessageByAuthor = nextAuthor != content.sender

            if (isFirstMessageByAuthor) {
                if (index == 0) {
                    view.sentTextMessageTime.text = DatesUtil.getTimeAgo(content.timeStamp)
                    view.sentTextMessageTime.show()
                } else {
                    view.sentTextMessageTime.hide()
                }
            }
        }
        view.setOnClickListener { onMessageClick(messages[position]._id) }
        if (messages[position].deleted) {
            view.sentTextMessage.text = view.context.getString(R.string.delete_for_all)
            view.sentTextMessage.setTypeface(  view.sentTextMessage.typeface,Typeface.ITALIC)
            view.sentTextMessage.setTextColor(Color.parseColor("#828282"))
            view.sentTextMessage.setBackgroundResource(R.drawable.sent_message_deleted_background)
        } else {
            view.sentTextMessage.text = (messages[position].message)
        }
    }

}