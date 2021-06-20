package com.gappein.coroutine.sdk.impl

import com.gappein.coroutine.sdk.Gappein
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.User

class GappeinImpl(private val client: ChatClient) : Gappein {

    override fun currentUser() = client.getUser()

    override fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {
        client.setUser(user, token,onSuccess,onError)
    }

}