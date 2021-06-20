package com.gappein.coroutine.sdk.client

import android.content.Context
import android.net.Uri
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManager
import com.gappein.coroutine.sdk.impl.ChatClientImpl
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.service.GappeinDbService

interface ChatClient {

    companion object {

        private var INSTANCE: ChatClient? = null

        @JvmStatic
        fun getInstance(): ChatClient = INSTANCE
            ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")

    }

    class Builder {

        private val gappeinDbService: GappeinDbService = GappeinDbService()

        private var storageManager: FirebaseStorageManager? = null

        private var apiKey: String = ""

        fun setApiKey(apiKey: String) = apply { this.apiKey = apiKey }

        fun setStorageManager(storageManager: FirebaseStorageManager) =
            apply { this.storageManager = storageManager }


        /**
         * Use this to use the methods of ChatClient
         *
         * @return Instance of ChatClient
         */
        fun build(): ChatClient {
            return storageManager?.let { storageManager ->
                ChatClientImpl(storageManager, gappeinDbService, apiKey).apply {
                    INSTANCE = this
                }
            } as ChatClient
        }
    }

    /**
     * Use to set the current User detail
     *
     * @param user - object of User.kt
     * @param token - String - unique id associated to the User
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */
    suspend fun setUser(user: User, token: String): User

    /**
     * Use to return instance of the current User detail
     *
     * @return object of User.kt
     */
    fun getUser(): User

    fun getApiKey(): String

    fun getBackupLink(
        context: Context,
        channelId: String
    ): String

    /**
     * Use to send text message
     *
     * @param messageText - String - message text
     * @param receiver - String - token of the receiving User
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */

    suspend fun sendMessage(
        messageText: String,
        receiver: String
    )

    /**
     * Use to send file URI
     *
     * @param fileUri - URI object of the file
     * @param receiver - String - token of the receiving User
     * @param onSuccess - Success callback
     * @param onProgress - File upload progress callback
     * @param onError - Error callback
     */
    suspend fun sendMessage(
        fileUri: Uri,
        receiver: String
    )

    /**
     * Use to get User for the respective token
     *
     * @param token - String - token of the User you want to fetch
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */
    suspend fun getUserByToken(token: String): User

    /**
     * Use to open/create a channel with given User
     *
     * @param participantUserToken - String - token of the User you want to open channel with
     * @param onComplete - on Complete callback
     */
    suspend fun openOrCreateChannel(
        participantUserToken: String
    ): String

    /**
     * Use to get all channels of the current user
     *
     * @param onSuccess - Success callback
     */
    suspend fun getUserChannels(): List<Channel>

    /**
     * Use to get Messages for given channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    suspend fun getMessages(channelId: String): List<Message>

    /**
     * Use to get Users for given channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    suspend fun getChannelUsers(channelId: String): List<User>

    /**
     * Use to get the recipient User of a channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    suspend fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit)

    /**
     * Use to get last message in a channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    suspend fun getLastMessageFromChannel(channelId: String): Pair<Message, User>

    /**
     * Use to check whether a particular User is online
     *
     * @param token - String - token of the User you want to check
     * @param onSuccess - Success callback
     */
    suspend fun isUserOnline(token: String): Pair<Boolean, String>

    /**
     * Use to set the User online
     *
     * @param token - String - token of the User you want to show online
     */
    suspend fun setUserOnline(
        status: Boolean
    )

    suspend fun getAllChannels(): List<Channel>

    suspend fun deleteMessage(
        channelId: String,
        message: Message
    )

    suspend fun likeMessage(
        channelId: String, messageId: String
    )

    suspend fun getTypingStatus(channelId: String, participantUserId: String): String

    suspend fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean
    )

    suspend fun setChatBackground(
        channelId: String,
        backgroundUrl: Uri
    )

    suspend fun getChatBackground(channelId: String): String

    suspend fun setUserStatus(status: String)

    suspend fun getUserStatus(
        token: String = getUser().token
    ): String
}
