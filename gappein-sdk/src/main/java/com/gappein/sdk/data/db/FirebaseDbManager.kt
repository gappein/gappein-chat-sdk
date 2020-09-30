package com.gappein.sdk.data.db

import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

interface FirebaseDbManager {

    fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun sendMessage(message: Message, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getUserByToken(token: String, onSuccess: (user: User) -> Unit, onError: (Exception) -> Unit)

    fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onSuccess: (channelId: String) -> Unit
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

    fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit)

    fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit)

    fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit)

    fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit)

    fun setUserOnline(token: String)

    fun getAllChannels(onSuccess: (List<Channel>) -> Unit)

    fun deleteMessage(channelId: String, messageId: String, onSuccess: () -> Unit)

    fun setTypingStatus(channelId: String, userId: String, status: Boolean, onSuccess: () -> Unit)

}