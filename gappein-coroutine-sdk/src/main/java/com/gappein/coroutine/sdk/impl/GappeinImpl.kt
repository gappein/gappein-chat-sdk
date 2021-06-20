package com.gappein.coroutine.sdk.impl

import com.gappein.coroutine.sdk.Gappein
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.User

class GappeinImpl(private val client: ChatClient) : Gappein {

    override suspend fun currentUser() = client.getUser()

    override suspend fun setUser(user: User, token: String): User? {
        return client.setUser(user, token).data
    }

}