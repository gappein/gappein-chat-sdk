package com.gappein.coroutine.sdk.util

import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.ChannelListData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun Channel.toChannelList(): ChannelListData {
    val channelId = this.id
    val client = ChatClient.getInstance()
    val lastMessageFromChannel = client.getLastMessageFromChannel(channelId)
    return suspendCoroutine { continuation ->
        val channelListData = ChannelListData(
            id = channelId,
            user = lastMessageFromChannel.second,
            lastMessage = lastMessageFromChannel.first
        )
        continuation.resume(channelListData)
    }
}


suspend fun Channel.toChannelList(client: ChatClient): ChannelListData {
    val channelId = this.id
    val lastMessageFromChannel = client.getLastMessageFromChannel(channelId)
    return suspendCoroutine { continuation ->
        val channelListData = ChannelListData(
            id = channelId,
            user = lastMessageFromChannel.second,
            lastMessage = lastMessageFromChannel.first
        )
        continuation.resume(channelListData)
    }
}