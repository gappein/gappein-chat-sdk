package com.gappein.sdk

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.db.FirebaseDbManagerImpl
import com.gappein.sdk.data.storage.FirebaseStorageManager
import com.gappein.sdk.data.storage.FirebaseStorageManagerImpl
import com.gappein.sdk.impl.GappeinImpl
import com.gappein.sdk.model.User

interface Gappein {

    companion object {

        private var instance: Gappein? = null

        @JvmStatic
        fun getInstance(): Gappein = instance ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")
    }

    fun currentUser(): User

    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    class Builder() {

        private val firebaseDbManager: FirebaseDbManager = FirebaseDbManagerImpl()
        private val firebaseStorageManager: FirebaseStorageManager = FirebaseStorageManagerImpl()

        /**
         * Use this to initialize the SDK in the Application class
         *
         * @return Instance of Gappein-Chat-SDK
         */
        fun build(): Gappein =  GappeinImpl(ChatClient.Builder(firebaseStorageManager, firebaseDbManager).build()).apply {
                instance = this
        }
    }
}