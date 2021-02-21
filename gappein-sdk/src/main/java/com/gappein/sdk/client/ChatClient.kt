package com.gappein.sdk.client

import android.content.Context
import android.net.Uri
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.storage.FirebaseStorageManager
import com.gappein.sdk.impl.ChatClientImpl
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

interface ChatClient {

    companion object {

        private var INSTANCE: ChatClient? = null

        @JvmStatic
        fun getInstance(): ChatClient = INSTANCE
            ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")

    }

    class Builder {

        private var dbManager: FirebaseDbManager? = null

        private var storageManager: FirebaseStorageManager? = null

        private var apiKey: String = ""

        fun setDatabaseManager(dbManager: FirebaseDbManager) = apply { this.dbManager = dbManager }

        fun setApiKey(apiKey: String) = apply { this.apiKey = apiKey }

        fun setStorageManager(storageManager: FirebaseStorageManager) =
            apply { this.storageManager = storageManager }

        /**
         * Use this to use the methods of ChatClient
         *
         * @return Instance of ChatClient
         */
        fun build(): ChatClient = storageManager?.let { storageManager ->
            dbManager?.let { dbManager ->
                ChatClientImpl(storageManager, dbManager, apiKey).apply {
                    INSTANCE = this
                }
            }
        } as ChatClient
    }

    /**
     * Use to set the current User detail
     *
     * @param user - object of User.kt
     * @param token - String - unique id associated to the User
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */
    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    /**
     * Use to return instance of the current User detail
     *
     * @return object of User.kt
     */
    fun getUser(): User

    fun getApiKey(): String

    fun getBackupLink(
        context: Context,
        channelId: String,
        onSuccess: (String) -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    )

    /**
     * Use to send text message
     *
     * @param messageText - String - message text
     * @param receiver - String - token of the receiving User
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */
    fun sendMessage(
        messageText: String,
        receiver: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
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
    fun sendMessage(
        fileUri: Uri,
        receiver: String,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    )

    /**
     * Use to get User for the respective token
     *
     * @param token - String - token of the User you want to fetch
     * @param onSuccess - Success callback
     * @param onError - Error callback
     */
    fun getUserByToken(token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    /**
     * Use to open/create a channel with given User
     *
     * @param participantUserToken - String - token of the User you want to open channel with
     * @param onComplete - on Complete callback
     */
    fun openOrCreateChannel(participantUserToken: String, onComplete: (channelId: String) -> Unit)

    /**
     * Use to get all channels of the current user
     *
     * @param onSuccess - Success callback
     */
    fun getUserChannels(onSuccess: (List<Channel>) -> Unit)

    /**
     * Use to get Messages for given channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit)

    /**
     * Use to get Users for given channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit)

    /**
     * Use to get the recipient User of a channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit)

    /**
     * Use to get last message in a channel
     *
     * @param channelId - String - channel id
     * @param onSuccess - Success callback
     */
    fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit)

    /**
     * Use to check whether a particular User is online
     *
     * @param token - String - token of the User you want to check
     * @param onSuccess - Success callback
     */
    fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit)

    /**
     * Use to set the User online
     *
     * @param token - String - token of the User you want to show online
     */
    fun setUserOnline(token: String,status:Boolean)

    fun getAllChannels(onSuccess: (List<Channel>) -> Unit)

    fun deleteMessage(channelId: String, message:Message, onSuccess: () -> Unit,onError: (Exception) -> Unit)

    fun likeMessage(channelId: String, messageId: String, onSuccess: () -> Unit)

    fun getTypingStatus(channelId: String, participantUserId: String, onSuccess: (String) -> Unit)

    fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean,
        onSuccess: () -> Unit
    )

    fun setChatBackground(
        channelId: String,
        backgroundUrl: Uri,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    )

    fun getChatBackground(channelId: String, onSuccess: (String) -> Unit)

    fun setUserStatus(status: String, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getUserStatus(
        token: String = getUser().token,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    )
}
