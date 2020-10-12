package com.gappein.sdk.impl

import android.net.Uri
import android.webkit.URLUtil
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.storage.FirebaseStorageManager
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

class ChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: FirebaseDbManager
) : ChatClient {

    private var currentUser = User()

    override fun setUser(
        user: User,
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        currentUser = user
        dbManager.createUser(user, {
            onSuccess(it)
        }, {
            onError(it)
        })
    }


    override fun getUser() = currentUser

    override fun sendMessage(
        messageText: String,
        receiver: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        getUserByToken(receiver, {

            val message = Message(
                timeStamp = System.currentTimeMillis(),
                message = messageText,
                isUrl = URLUtil.isValidUrl(messageText),
                receiver = it,
                sender = getUser()
            )

            dbManager.sendMessageByToken(message, getUser(), it, onSuccess, onError)

        }, {
            onError(it)
        })
    }

    override fun sendMessage(
        fileUri: Uri,
        receiver: String,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {

        getUserByToken(receiver, { user ->
            storageManager.uploadMessageImage(fileUri, user, getUser(), { message ->
                sendMessage(message, receiver, { onSuccess() }, { onError(it) })
            }, { progress ->
                if (progress == 100) {
                    onSuccess()
                } else {
                    onProgress(progress)
                }
            }, { exception -> onError(exception) })
        }, { exception -> onError(exception) })

    }

    override fun getUserByToken(
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.getUserByToken(token, onSuccess, onError)
    }

    override fun openOrCreateChannel(
        participantUserToken: String,
        onComplete: (channelId: String) -> Unit
    ) {
        dbManager.getOrCreateNewChatChannels(participantUserToken, onComplete)
    }

    override fun getUserChannels(onSuccess: (List<Channel>) -> Unit) {
        dbManager.getUserChannels(onSuccess)
    }

    override fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit) {
        dbManager.getMessages(channelId, onSuccess)
    }

    override fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit) {
        dbManager.getChannelUsers(channelId, onSuccess)
    }

    override fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit) {
        dbManager.getChannelRecipientUser(channelId, onSuccess)
    }

    override fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit) {
        dbManager.getLastMessageFromChannel(channelId, onSuccess)
    }

    override fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit) {
        dbManager.isUserOnline(token, onSuccess)
    }

    override fun setUserOnline(token: String) {
        dbManager.setUserOnline(token)
    }

    override fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean,
        onSuccess: () -> Unit
    ) {
        dbManager.setTypingStatus(channelId, userId, isUserTyping, onSuccess)
    }

    override fun setChatBackground(
        channelId: String,
        backgroundUrl: Uri,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        storageManager.uploadChatBackgroundImage(backgroundUrl, channelId, {
            dbManager.setChatBackground(channelId, it, onSuccess, onError)
        }, {
            onProgress(it)
        }, {
            onError(it)
        })
    }

    override fun getChatBackground(channelId: String, onSuccess: (String) -> Unit) {
        dbManager.getChatBackground(channelId, onSuccess)
    }

    override fun setUserStatus(
        status: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.setUserStatus(status, onSuccess, onError)
    }

    override fun getUserStatus(
        token: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.getUserStatus(token, onSuccess, onError)
    }

    override fun getAllChannels(onSuccess: (List<Channel>) -> Unit) {
        dbManager.getAllChannels(onSuccess)
    }

    override fun deleteMessage(channelId: String, messageId: String, onSuccess: () -> Unit) {
        dbManager.deleteMessage(channelId, messageId, onSuccess)
    }

    override fun likeMessage(channelId: String, messageId: String, onSuccess: () -> Unit) {
        dbManager.likeMessage(channelId, messageId, onSuccess)
    }

    override fun getTypingStatus(
        channelId: String,
        participantUserId: String,
        onSuccess: (String) -> Unit
    ) {
        dbManager.getTypingStatus(channelId, participantUserId, onSuccess)
    }
}