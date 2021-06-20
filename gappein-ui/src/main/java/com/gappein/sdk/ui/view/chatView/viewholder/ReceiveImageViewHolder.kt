package com.gappein.sdk.ui.view.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.databinding.ItemImageReceivedMessageBinding
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.onDoubleTapListener
import com.gappein.sdk.ui.view.util.show

class ReceiveImageViewHolder(private val binding: ItemImageReceivedMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val GIPHY = "giphy"
        private const val SPACE_SPLITTER = " "
    }

    fun bind(
        message: Message,
        onImageClick: (String) -> Unit,
        onMessageLike: (String) -> Unit
    ) {

        if (message.message.contains(GIPHY)) {
            if (ChatClient.getInstance().getApiKey().isNotEmpty()) {
                val _message = message.message
                val listOfMessages = _message.split(SPACE_SPLITTER)

                binding.gifView.setMediaWithId(listOfMessages.last())
                binding.gifView.show()
                binding.receivedImageMessage.hide()
            } else {
                binding.run {
                    gifView.hide()
                    receivedImageMessage.hide()
                }
            }
        } else {
            Glide.with(binding.root)
                .load(message.message)
                .transform(CenterCrop(), RoundedCorners(48))
                .into(binding.receivedImageMessage)
            binding.receivedImageMessage.show()
            binding.gifView.hide()
        }

        binding.likeImageView.visibility = if (message.liked) View.VISIBLE else View.INVISIBLE

        binding.receivedImageMessage.setOnClickListener { onImageClick.invoke(message.message) }

        if (!message.deleted) {
            binding.root.onDoubleTapListener {
                onMessageLike(message._id)
            }
        }
    }
}