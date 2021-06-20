package com.gappein.coroutine.sdk

import android.content.Context
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManager
import com.gappein.coroutine.sdk.data.storage.FirebaseStorageManagerImpl
import com.gappein.coroutine.sdk.impl.GappeinImpl
import com.gappein.coroutine.sdk.model.User
import com.giphy.sdk.ui.Giphy

interface Gappein {

    companion object {

        private val firebaseStorageManager: FirebaseStorageManager by lazy { FirebaseStorageManagerImpl() }

        private var INSTANCE: Gappein? = null

        private const val EMPTY_STRING = ""

        @JvmStatic
        fun getInstance(): Gappein = INSTANCE
            ?: throw IllegalStateException("Gappein.initialize() must be called before obtaining Gappein instance")


        /**
         * Use this to initialize the SDK in the Application class
         *
         * @return Instance of Gappein-Chat-SDK
         */

        @JvmStatic
        fun initialize(context: Context, apiKey: String = EMPTY_STRING): Gappein {
            return GappeinImpl(
                ChatClient.Builder()
                    .setApiKey(apiKey)
                    .setStorageManager(firebaseStorageManager)
                    .build()
            ).apply {
                INSTANCE = this
                Giphy.configure(context.applicationContext, apiKey)
            }
        }
    }

    fun currentUser(): User

    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

}