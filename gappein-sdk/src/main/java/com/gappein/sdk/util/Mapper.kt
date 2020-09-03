package com.gappein.sdk.util

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.ChannelListData

fun Channel.toChannelList(onSuccess: (ChannelListData) -> Unit) {
    val channelId = this.id

    ChatClient.getInstance().getLastMessageFromChannel(channelId) { message, user ->

        val channelListData = ChannelListData(
            id = channelId,
            user = user,
            lastMessage = message
        )
        onSuccess(channelListData)
    }
}