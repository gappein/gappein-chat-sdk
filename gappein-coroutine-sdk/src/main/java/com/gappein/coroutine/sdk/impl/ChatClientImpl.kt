package com.gappein.coroutine.sdk.impl

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManager
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.UploadResponse
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.service.GappeinDbService
import com.gappein.coroutine.sdk.util.BaseResponse
import com.gappein.coroutine.sdk.util.getFile
import com.google.gson.Gson

class ChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: GappeinDbService,
    private val apiKey: String
) : ChatClient {

    private var currentUser = User()

    override suspend fun setUser(user: User, token: String): BaseResponse<User> {
        currentUser = user
        return dbManager.userService.createUser(user)
    }

    override suspend fun getUser(): User {
        return currentUser
    }

    override suspend fun getApiKey(): String {
        return apiKey
    }

    override suspend fun getBackupLink(context: Context, channelId: String): UploadResponse {
        val response = dbManager.channelService.getBackupMessages(channelId)
        val file = Gson().getFile(context, channelId, response)
        return storageManager.uploadBackupChat(file, channelId)
    }

    override suspend fun sendMessage(messageText: String, receiver: String): Boolean {
        val user = getUserByToken(receiver)
        val message = Message(
            timeStamp = System.currentTimeMillis(),
            message = messageText,
            isUrl = URLUtil.isValidUrl(messageText),
            receiver = user,
            sender = getUser()
        )
        return dbManager.channelService.sendMessageByToken(message, getUser(), user)
    }

    override suspend fun sendMessage(fileUri: Uri, receiver: String): UploadResponse {
        val user = getUserByToken(receiver)
        return storageManager.uploadMessageImage(fileUri, user, getUser())
    }

    override suspend fun getUserByToken(token: String): User {
        return dbManager.userService.getUserByToken(token)
    }

    override suspend fun openOrCreateChannel(participantUserToken: String): String {
        return dbManager.channelService.getOrCreateNewChatChannels(participantUserToken)
    }

    override suspend fun getUserChannels(): List<Channel> {
        return dbManager.channelService.getUserChannels()
    }

    override suspend fun getMessages(channelId: String): List<Message> {
        return dbManager.channelService.getMessages(channelId)
    }

    override suspend fun getChannelUsers(channelId: String): List<User> {
        return dbManager.channelService.getChannelUsers(channelId)
    }

    override suspend fun getChannelRecipientUser(channelId: String): User {
        return dbManager.channelService.getChannelRecipientUser(channelId)
    }

    override suspend fun getLastMessageFromChannel(channelId: String): Pair<Message, User> {
        return dbManager.channelService.getLastMessageFromChannel(channelId)
    }

    override suspend fun isUserOnline(token: String): Pair<Boolean, String> {
        return dbManager.channelService.isUserOnline(token)
    }

    override suspend fun setUserOnline(status: Boolean): Boolean {
        return dbManager.userService.setUserOnline(status)
    }

    override suspend fun getAllChannels(): List<Channel> {
        return dbManager.channelService.getAllChannels()
    }

    override suspend fun deleteMessage(channelId: String, message: Message): Boolean {
        return dbManager.channelService.deleteMessage(channelId, message)
    }

    override suspend fun likeMessage(channelId: String, messageId: String) {
        return dbManager.channelService.likeMessage(channelId, messageId)
    }

    override suspend fun getTypingStatus(channelId: String, participantUserId: String): String {
        return dbManager.channelService.getTypingStatus(channelId, participantUserId)
    }

    override suspend fun setTypingStatus(channelId: String, userId: String, isUserTyping: Boolean) {
        return dbManager.channelService.setTypingStatus(channelId, userId, isUserTyping)
    }

    override suspend fun setChatBackground(channelId: String, backgroundUrl: Uri) {
        val response = storageManager.uploadChatBackgroundImage(backgroundUrl, channelId)
        dbManager.channelService.setChatBackground(channelId, response.url)
    }

    override suspend fun getChatBackground(channelId: String): String {
        return dbManager.channelService.getChatBackground(channelId)
    }

    override suspend fun setUserStatus(status: String): Boolean {
        return dbManager.userService.setUserStatus(status)
    }

    override suspend fun getUserStatus(token: String): String {
        return dbManager.userService.getUserStatus(token)
    }

}