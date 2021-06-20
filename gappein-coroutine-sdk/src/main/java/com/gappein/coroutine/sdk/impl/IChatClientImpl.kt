package com.gappein.coroutine.sdk.impl

import android.content.Context
import android.net.Uri
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManager
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.service.GappeinDbService

class IChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: GappeinDbService,
    private val apiKey: String
) : ChatClient {
    override suspend fun setUser(user: User, token: String): User {

    }

    override fun getUser(): User {
        TODO("Not yet implemented")
    }

    override fun getApiKey(): String {
        TODO("Not yet implemented")
    }

    override fun getBackupLink(context: Context, channelId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(messageText: String, receiver: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(fileUri: Uri, receiver: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByToken(token: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun openOrCreateChannel(participantUserToken: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUserChannels(): List<Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(channelId: String): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelUsers(channelId: String): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastMessageFromChannel(channelId: String): Pair<Message, User> {
        TODO("Not yet implemented")
    }

    override suspend fun isUserOnline(token: String): Pair<Boolean, String> {
        TODO("Not yet implemented")
    }

    override suspend fun setUserOnline(status: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllChannels(): List<Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(channelId: String, message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun likeMessage(channelId: String, messageId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getTypingStatus(channelId: String, participantUserId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun setTypingStatus(channelId: String, userId: String, isUserTyping: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setChatBackground(channelId: String, backgroundUrl: Uri) {
        TODO("Not yet implemented")
    }

    override suspend fun getChatBackground(channelId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun setUserStatus(status: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserStatus(token: String): String {
        TODO("Not yet implemented")
    }

}