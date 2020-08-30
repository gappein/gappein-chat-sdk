package com.gappein.sdk

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.impl.GappeinImpl
import com.gappein.sdk.model.User
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.db.FirebaseDbManagerImpl
import com.gappein.sdk.data.storage.FirebaseStorageManager
import com.gappein.sdk.data.storage.FirebaseStorageManagerImpl

interface Gappein {

    fun currentUser(): User

    fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

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