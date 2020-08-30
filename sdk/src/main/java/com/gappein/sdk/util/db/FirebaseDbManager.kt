package com.gappein.sdk.util.db

import com.gappein.sdk.model.User

interface FirebaseDbManager {

    fun createUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    )

    fun sendMessage(
        user: User,
        message: String,
        isUrl: Boolean = false,
        onSuccess: () -> Boolean,
        onError: () -> Boolean
    )
}