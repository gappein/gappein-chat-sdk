package com.gappein.sdk.service.channel

import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
interface ChannelService {

    fun sendMessage(message: Message, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onSuccess: (channelId: String) -> Unit,
        onError: (Exception) -> Unit
    )

    fun getUserChannels(onSuccess: (List<Channel>) -> Unit)

    fun sendMessageByToken(
        message: Message,
        sender: User,
        receiver: User,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit)

    fun getBackupMessages(channelId: String, onSuccess: (List<Message>) -> Unit)

    fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit)

    fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit)

    fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit)

    fun getAllChannels(onSuccess: (List<Channel>) -> Unit)

    fun deleteMessage(
        channelId: String,
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean,
        onSuccess: () -> Unit
    )

    fun getTypingStatus(channelId: String, participantUserId: String, onSuccess: (String) -> Unit)

    fun setChatBackground(
        channelId: String,
        backgroundUrl: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun getChatBackground(channelId: String, onSuccess: (String) -> Unit)

    fun likeMessage(
        channelId: String,
        messageId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit)


}