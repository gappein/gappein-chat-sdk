package com.gappein.sdk.data.db

import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

interface FirebaseDbManager {

    fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun sendMessage(message: Message, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getUserByToken(token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun getOrCreateNewChatChannels(participantUserToken: String, onComplete: (channelId: String) -> Unit)

    fun getAllChannel(onSuccess: (List<String>) -> Unit)

    fun sendMessageByToken(message: Message, sender: User, receiver: User, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getMessages(channelId: String,onSuccess: (List<Message>) -> Unit)
}