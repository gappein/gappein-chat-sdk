package com.gappein.coroutine.sdk.impl

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManager
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.service.GappeinDbService
import com.gappein.coroutine.sdk.util.getFile
import com.google.gson.Gson

class ChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: GappeinDbService,
    private val apiKey: String
) : ChatClient {

    private var currentUser = User()

    override fun setUser(
        user: User,
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        currentUser = user
        dbManager.userService.createUser(user, {
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override fun getUser() = currentUser

    override fun getApiKey() = apiKey

    override fun getBackupLink(
        context: Context,
        channelId: String,
        onSuccess: (String) -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.channelService.getBackupMessages(channelId) {
            val file = Gson().getFile(context, channelId, it)
            storageManager.uploadBackupChat(file, channelId, onSuccess, onProgress, onError)
        }
    }

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
            dbManager.channelService.sendMessageByToken(message, getUser(), it, onSuccess, onError)
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
        dbManager.userService.getUserByToken(token, onSuccess, onError)
    }

    override fun openOrCreateChannel(
        participantUserToken: String,
        onComplete: (channelId: String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.channelService.getOrCreateNewChatChannels(
            participantUserToken,
            onComplete,
            onError
        )
    }

    override fun getUserChannels(onSuccess: (List<Channel>) -> Unit) {
        dbManager.channelService.getUserChannels(onSuccess)
    }

    override fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit) {
        dbManager.channelService.getMessages(channelId, onSuccess)
    }

    override fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit) {
        dbManager.channelService.getChannelUsers(channelId, onSuccess)
    }

    override fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit) {
        dbManager.channelService.getChannelRecipientUser(channelId, onSuccess)
    }

    override fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit) {
        dbManager.channelService.getLastMessageFromChannel(channelId, onSuccess)
    }

    override fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit) {
        dbManager.channelService.isUserOnline(token, onSuccess)
    }

    override fun setUserOnline(
        status: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.userService.setUserOnline(status, onSuccess, onError)
    }

    override fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean,
        onSuccess: () -> Unit
    ) {
        dbManager.channelService.setTypingStatus(channelId, userId, isUserTyping, onSuccess)
    }

    override fun setChatBackground(
        channelId: String,
        backgroundUrl: Uri,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        storageManager.uploadChatBackgroundImage(backgroundUrl, channelId, {
            dbManager.channelService.setChatBackground(channelId, it, onSuccess, onError)
        }, {
            onProgress(it)
        }, {
            onError(it)
        })
    }

    override fun getChatBackground(channelId: String, onSuccess: (String) -> Unit) {
        dbManager.channelService.getChatBackground(channelId, onSuccess)
    }

    override fun setUserStatus(
        status: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.userService.setUserStatus(status, onSuccess, onError)
    }

    override fun getUserStatus(
        token: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.userService.getUserStatus(token, onSuccess, onError)
    }

    override fun getAllChannels(onSuccess: (List<Channel>) -> Unit) {
        dbManager.channelService.getAllChannels(onSuccess)
    }

    override fun deleteMessage(
        channelId: String,
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.channelService.deleteMessage(channelId, message, onSuccess, onError)
    }

    override fun likeMessage(
        channelId: String,
        messageId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.channelService.likeMessage(channelId, messageId, onSuccess, onError)
    }

    override fun getTypingStatus(
        channelId: String,
        participantUserId: String,
        onSuccess: (String) -> Unit
    ) {
        dbManager.channelService.getTypingStatus(channelId, participantUserId, onSuccess)
    }
}