package com.gappein.sdk.util.db

import com.gappein.sdk.model.User
import com.google.firebase.database.FirebaseDatabase

class FirebaseDbManagerImpl : FirebaseDbManager {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        private const val USER_DB = "users"
        private const val MESSAGES_DB = "messages"
    }

    override fun createUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userPath = USER_DB + "/" + user.token
        database.getReference(userPath).setValue(user)
            .addOnSuccessListener { onSuccess(user) }
            .addOnFailureListener { onError(it) }
    }


    override fun sendMessage(
        user: User,
        message: String,
        isUrl: Boolean,
        onSuccess: () -> Boolean,
        onError: () -> Boolean
    ) {

    }
}