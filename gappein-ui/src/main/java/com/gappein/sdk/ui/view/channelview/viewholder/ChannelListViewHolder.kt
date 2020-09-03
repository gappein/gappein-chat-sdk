package com.gappein.sdk.ui.view.channelview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.mapper.toChannelList
import com.gappein.sdk.model.Channel
import com.gappein.sdk.ui.view.util.DatesUtil
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(channel: Channel) {

        val channelData = channel.toChannelList {
            Glide.with(view)
                .load("https://static.toiimg.com/thumb/msid-69902898,imgsize-115506,width-800,height-600,resizemode-75/69902898.jpg")
                .transform(CenterCrop(), RoundedCorners(100))
                .into(view.imageViewAvatar)
            view.textViewUserName.setText(it.users.name)
            view.textViewLastMessage.setText(it.lastMessage.message)
            view.textViewLastMessageTime.setText(DatesUtil.getTimeAgo(it.lastMessage.timeStamp))
        }
    }

}