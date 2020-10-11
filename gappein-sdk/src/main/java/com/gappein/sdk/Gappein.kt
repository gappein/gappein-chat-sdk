package com.gappein.sdk

import android.content.Context
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.db.FirebaseDbManagerImpl
import com.gappein.sdk.data.storage.FirebaseStorageManager
import com.gappein.sdk.data.storage.FirebaseStorageManagerImpl
import com.gappein.sdk.impl.GappeinImpl
import com.gappein.sdk.model.User
import com.giphy.sdk.ui.Giphy

interface Gappein {

    companion object {

        private val firebaseDbManager: FirebaseDbManager = FirebaseDbManagerImpl()
        private val firebaseStorageManager: FirebaseStorageManager = FirebaseStorageManagerImpl()
        private var INSTANCE: Gappein? = null
        public var isAPIProvided : String = ""

        @JvmStatic
        fun getInstance(): Gappein = INSTANCE
            ?: throw IllegalStateException("Gappein.initialize() must be called before obtaining Gappein instance")


        /**
         * Use this to initialize the SDK in the Application class
         *
         * @return Instance of Gappein-Chat-SDK
         */

        @JvmStatic
        fun initialize(context: Context, apiKey: String = ""): Gappein {


            return GappeinImpl(
                ChatClient.Builder()
                    .setDatabaseManager(firebaseDbManager)
                    .setStorageManager(firebaseStorageManager)
                    .build()
            ).apply {
                INSTANCE = this
                if(apiKey.isNotEmpty()) {
                    isAPIProvided = "YES"
                    Giphy.configure(context, apiKey)
                }
            }
        }

        @JvmStatic
        fun initialize(): Gappein {
            return GappeinImpl(
                ChatClient.Builder()
                    .setDatabaseManager(firebaseDbManager)
                    .setStorageManager(firebaseStorageManager)
                    .build()
            ).apply {
                INSTANCE = this
            }
        }

    }

    fun currentUser(): User

    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

}