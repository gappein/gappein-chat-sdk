package com.gappein.sdk.util.db

import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User

interface FirebaseDbManager {

    fun createUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    )

    fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )
}