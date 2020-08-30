package com.gappein.sdk

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.impl.GappeinImpl
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.db.FirebaseDbManagerImpl
import com.gappein.sdk.util.storage.FirebaseStorageManager
import com.gappein.sdk.util.storage.FirebaseStorageManagerImpl

interface Gappein {

    fun currentUser(): User

    fun setUser(
        user: User,
        token: String,
        callbacks: InitConnectionListener = object : InitConnectionListener() {}
    )

    class Builder {

        private val firebaseDbManager: FirebaseDbManager = FirebaseDbManagerImpl()
        private val firebaseStorageManager: FirebaseStorageManager = FirebaseStorageManagerImpl()

        fun build(): Gappein {
            return GappeinImpl(
                ChatClient.Builder(
                    firebaseStorageManager,
                    firebaseDbManager
                ).build()
            ).apply {
                instance = this
            }
        }
    }

    companion object {

        private var instance: Gappein? = null

        @JvmStatic
        fun getInstance(): Gappein = instance
            ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")
    }
}