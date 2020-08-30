package com.gappein.sdk.client

import android.net.Uri
import com.gappein.sdk.impl.ChatClientImpl
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.storage.FirebaseStorageManager

interface ChatClient {

    companion object {

        private var instance: ChatClient? = null

        @JvmStatic
        fun instance(): ChatClient = instance ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")

    }

    class Builder(private val storageManager: FirebaseStorageManager, private val dbManager: FirebaseDbManager) {

        fun build(): ChatClient = ChatClientImpl(storageManager, dbManager).apply {
                instance = this
        }
    }

    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun getUser(): User

    fun sendMessage(messageText: String, receiver: String, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun sendMessage(fileUri: Uri, receiver: String, onSuccess: () -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit)

    fun getUserByToken(token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun openOrCreateChannel(participantUserToken: String, onComplete: (channelId: String) -> Unit)

    fun getAllChannels(onSuccess: (List<String>) -> Unit)

}