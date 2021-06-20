package com.gappein.sdk.impl

import com.gappein.sdk.Gappein
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.User

class GappeinImpl(private val client: ChatClient) : Gappein {

    override fun currentUser() = client.getUser()

    override fun setUser(
        user: User,
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        client.setUser(user, token, onSuccess, onError)
    }

}