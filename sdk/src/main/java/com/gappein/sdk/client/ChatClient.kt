package com.gappein.sdk.client

import android.net.Uri
import com.gappein.sdk.impl.ChatClientImpl
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.storage.FirebaseStorageManager

interface ChatClient {

    companion object {

        private var instance: ChatClient? = null

        @JvmStatic
        fun instance(): ChatClient {
            return instance
                ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")
        }
    }

    class Builder(
        private val storageManager: FirebaseStorageManager,
        private val dbManager: FirebaseDbManager
    ) {

        fun build(): ChatClient {
            return ChatClientImpl(
                storageManager,
                dbManager
            ).apply {
                instance = this
            }
        }
    }

    fun setUser(user: User, token: String, listener: InitConnectionListener? = null)

    fun getUser(): User

    fun sendMessage(
        message: String,
        receiver: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )

    fun sendMessage(
        fileUri: Uri,
        receiver: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )

}