package com.gappein.sdk.impl

import androidx.lifecycle.MutableLiveData
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.storage.FirebaseStorageManager

class ChatClientImpl(
    private val firebaseStorageManager: FirebaseStorageManager,
    private val firebaseDbManager: FirebaseDbManager
) : ChatClient {

    private val currentUser = MutableLiveData<User>()

    override fun setUser(user: User, token: String, listener: InitConnectionListener?) {
        currentUser.postValue(user)
        firebaseDbManager.createUser(user, {
            listener?.onSuccess(InitConnectionListener.ConnectionData(it, token))
        }, {
            listener?.onError(it.localizedMessage ?: "User already present")
        })
    }

    override fun getCurrentUser() = currentUser

}