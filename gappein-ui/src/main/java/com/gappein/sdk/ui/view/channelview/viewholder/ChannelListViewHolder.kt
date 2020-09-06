package com.gappein.sdk.ui.view.channelview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.util.toChannelList
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        channel: Channel,
        onUserClick: (User) -> Unit,
        onChannelClick: (Channel, User) -> Unit
    ) {

        channel.toChannelList { data ->
            Glide.with(view)
                .load("https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=934&q=80")
                .transform(CenterCrop(), RoundedCorners(100))
                .into(view.imageViewAvatar)
            view.textViewUserName.text = data.user.name

            view.textViewLastMessage.text = data.lastMessage.message

            view.textViewLastMessageTime.text = DatesUtil.getTimeAgo(data.lastMessage.timeStamp)

            view.imageViewAvatar.setOnClickListener {
                onUserClick(data.user)
            }
            view.setOnClickListener {
                onChannelClick.invoke(channel, data.user)
            }

            ChatClient.getInstance().isUserOnline(data.user.token) { isOnline, lastTimeStamp ->
                if (isOnline) {
                    Glide.with(view)
                        .load(R.drawable.ic_online_indicator)
                        .transform(CenterCrop(), RoundedCorners(100))
                        .into(view.imageViewOnline)
                }
            }
        }

    }

}