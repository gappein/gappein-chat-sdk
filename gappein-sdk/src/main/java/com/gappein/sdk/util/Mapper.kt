package com.gappein.sdk.util

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.ChannelListData

fun Channel.toChannelList(onSuccess: (ChannelListData) -> Unit) {
    val channelId = this.id
    val client = ChatClient.getInstance()
    client.getLastMessageFromChannel(channelId) { message, user ->

        val channelListData = ChannelListData(
            id = channelId,
            user = user,
            lastMessage = message,
            isUserOnline = user.isOnline
        )
        onSuccess(channelListData)
    }
}