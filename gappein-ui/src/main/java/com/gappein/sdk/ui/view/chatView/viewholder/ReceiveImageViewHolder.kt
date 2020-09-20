package com.gappein.sdk.ui.view.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.util.onDoubleTapListener
import kotlinx.android.synthetic.main.item_image_received_message.view.*

class ReceiveImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        message: Message,
        position: Int,
        onImageClick: (String) -> Unit,
        onMessageLike: (String) -> Unit
    ) {
        Glide.with(view)
            .load(message.message)
            .placeholder(R.drawable.ic_user_placeholder)
            .transform(CenterCrop(), RoundedCorners(48))
            .into(view.receivedImageMessage)

        view.likeImageView.visibility = if (message.liked) View.VISIBLE else View.INVISIBLE

        view.receivedImageMessage.setOnClickListener { onImageClick.invoke(message.message) }

        if (!message.deleted) {
            view.onDoubleTapListener {
                onMessageLike(message._id)
            }
        }
    }
}