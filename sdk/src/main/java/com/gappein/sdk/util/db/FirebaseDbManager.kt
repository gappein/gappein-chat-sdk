package com.gappein.sdk.util.db

import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

interface FirebaseDbManager {

    fun createUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    )

    fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun getUserByToken(token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onComplete: (channelId: String) -> Unit
    )

    fun getAllChannel(onSuccess: (List<String>) -> Unit)
}