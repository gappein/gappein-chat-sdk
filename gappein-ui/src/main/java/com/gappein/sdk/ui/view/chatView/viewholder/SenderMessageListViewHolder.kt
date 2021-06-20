package com.gappein.sdk.ui.view.chatView.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.ItemSentMessageBinding
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.onDoubleTapListener
import com.gappein.sdk.ui.view.util.show

class SenderMessageListViewHolder(private val binding: ItemSentMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        position: Int,
        messages: List<Message>,
        onMessageClick: (Message) -> Unit,
        onMessageLike: (String) -> Unit
    ) {

        messages.forEachIndexed { index, content ->
            val prevAuthor = messages.getOrNull(position.plus(1))?.sender
            val nextAuthor = messages.getOrNull(position.minus(1))?.sender
            val isFirstMessageByAuthor = prevAuthor != content.sender
            val isLastMessageByAuthor = nextAuthor != content.sender

            if (isFirstMessageByAuthor) {
                if (index == 0) {
                    binding.sentTextMessageTime.text = DatesUtil.getTimeAgo(content.timeStamp)
                    binding.sentTextMessageTime.show()
                } else {
                    binding.sentTextMessageTime.hide()
                }
            }
        }
        binding.sentTextMessage.setOnClickListener {
            onMessageClick(messages[position])
        }

        if (!messages[position].deleted) {
            binding.root.onDoubleTapListener { onMessageLike(messages[position]._id) }
        }

        binding.likeImageView.visibility =
            if (messages[position].liked) View.VISIBLE else View.INVISIBLE

        if (messages[position].deleted) {
            binding.sentTextMessage.text = binding.root.context.getString(R.string.delete_for_all)
            binding.sentTextMessage.setTypeface(binding.sentTextMessage.typeface, Typeface.ITALIC)
            binding.sentTextMessage.setTextColor(Color.parseColor("#d3d3d3"))
            binding.sentTextMessage.setBackgroundResource(R.drawable.sent_message_deleted_background)
        } else {
            binding.sentTextMessage.text = (messages[position].message)
        }
    }

}