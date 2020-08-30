package com.gappein.sdk.impl

import androidx.lifecycle.LiveData
import com.gappein.sdk.Gappein
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.User

class GappeinImpl(private val client: ChatClient) : Gappein {


    override fun currentUser(): LiveData<User> = client.getCurrentUser()

    override fun setUser(user: User, token: String, callbacks: InitConnectionListener) {
        client.setUser(user, token, callbacks)
    }
}