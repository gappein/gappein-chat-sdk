package com.gappein.sdk.ui.binding.chatbinding.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.ItemReceivedMessageBinding
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.onDoubleTapListener
import com.gappein.sdk.ui.view.util.show

class ReceiverMessageListViewHolder(private val binding: ItemReceivedMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(messages: List<Message>, position: Int, onMessageLike: (String) -> Unit) {
        messages.forEachIndexed { index, content ->

            val prevAuthor = messages.getOrNull(position - 1)?.sender
            val nextAuthor = messages.getOrNull(position + 1)?.sender
            val isFirstMessageByAuthor = prevAuthor != content.sender
            val isLastMessageByAuthor = nextAuthor != content.sender

            if (isFirstMessageByAuthor) {
                if (index == 0) {
                    binding.receivedTextMessageTime.text = DatesUtil.getTimeAgo(content.timeStamp)
                    binding.receivedTextMessageTime.show()
                } else binding.receivedTextMessageTime.hide()
            }
        }
        if (messages[position].deleted) {
            binding.receivedTextMessage.text =
                binding.root.context.getString(R.string.delete_by_user)
            binding.receivedTextMessage.setTypeface(
                binding.receivedTextMessage.typeface,
                Typeface.ITALIC
            )
            binding.receivedTextMessage.setTextColor(Color.parseColor("#828282"))
            binding.receivedTextMessage.setBackgroundResource(R.drawable.recieved_message_deleted_background)
        } else {
            binding.receivedTextMessage.text = (messages[position].message)
        }

        binding.likeImageView.visibility =
            if (messages[position].liked) View.VISIBLE else View.INVISIBLE

        if (!messages[position].deleted) {
            binding.root.onDoubleTapListener {
                onMessageLike(messages[position]._id)
            }
        }
    }
}