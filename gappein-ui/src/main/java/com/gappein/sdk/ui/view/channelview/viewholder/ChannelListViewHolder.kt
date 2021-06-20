package com.gappein.sdk.ui.view.channelview.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.ItemChannelBinding
import com.gappein.sdk.ui.view.util.DatesUtil
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import com.gappein.sdk.util.toChannelList


class ChannelListViewHolder(
    private val binding: ItemChannelBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        channel: Channel,
        onUserClick: (User) -> Unit,
        onChannelClick: (Channel, User) -> Unit
    ) {

        channel.toChannelList { data ->

            Glide.with(binding.root)
                .load(data.user.profileImageUrl)
                .transform(CenterCrop(), RoundedCorners(36))
                .placeholder(R.drawable.ic_user_placeholder)
                .into(binding.imageViewAvatar)

            binding.textViewUserName.text = data.user.name

            if (data.lastMessage.deleted) {
                binding.textViewLastMessage.text =
                    context.getString(R.string.message_has_been_deleted)
            } else {
                binding.textViewLastMessage.text = data.lastMessage.message
            }
            binding.textViewLastMessageTime.text = DatesUtil.getTimeAgo(data.lastMessage.timeStamp)

            binding.imageViewAvatar.setOnClickListener {
                onUserClick(data.user)
            }
            binding.root.setOnClickListener {
                onChannelClick.invoke(channel, data.user)
            }

            ChatClient.getInstance().isUserOnline(data.user.token) { isOnline, x ->
                if (isOnline) {
                    Glide.with(binding.root)
                        .load(R.drawable.ic_online_indicator)
                        .transform(CenterCrop(), RoundedCorners(100))
                        .into(binding.imageViewOnline)
                    binding.imageViewOnline.show()
                } else {
                    binding.imageViewOnline.hide()
                }
            }
        }

    }

}