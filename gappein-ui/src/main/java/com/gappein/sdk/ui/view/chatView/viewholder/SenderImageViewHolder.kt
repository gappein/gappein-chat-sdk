package com.gappein.sdk.ui.view.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.model.Message
import kotlinx.android.synthetic.main.item_image_sent_message.view.*

class SenderImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(message: Message, position: Int) {
        Glide.with(view)
            .load(message.message)
            .transform(CenterCrop(), RoundedCorners(48))
            .into(view.sentImageMessage)
    }
}