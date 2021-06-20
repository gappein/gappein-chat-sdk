package com.gappein.coroutine.sdk.service.channel

import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
interface ChannelService {

    suspend fun sendMessage(message: Message): Boolean

    suspend fun getOrCreateNewChatChannels(participantUserToken: String): String

    suspend fun getUserChannels(): List<Channel>

    suspend fun sendMessageByToken(message: Message, sender: User, receiver: User): Boolean

    suspend fun getMessages(channelId: String): List<Message>

    suspend fun getBackupMessages(channelId: String): List<Message>

    suspend fun getChannelUsers(channelId: String): List<User>

    suspend fun getLastMessageFromChannel(channelId: String): Pair<Message, User>

    suspend fun getChannelRecipientUser(channelId: String): User

    suspend fun getAllChannels(): List<Channel>

    suspend fun deleteMessage(channelId: String, message: Message): Boolean

    suspend fun setTypingStatus(channelId: String, userId: String, isUserTyping: Boolean)

    suspend fun getTypingStatus(channelId: String, participantUserId: String): String

    suspend fun setChatBackground(channelId: String, backgroundUrl: String)

    suspend fun getChatBackground(channelId: String): String

    suspend fun likeMessage(channelId: String, messageId: String)

    suspend fun isUserOnline(token: String): Pair<Boolean, String>

}