package com.gappein.sdk.util.db

import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDbManagerImpl : FirebaseDbManager {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val SPLITTER = "/"
        private const val USER_COLLECTION = "users"
        private const val MESSAGES_COLLECTION = "messages"
    }

    override fun createUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {

        database.collection(USER_COLLECTION).document(user.token)
            .set(user)
            .addOnSuccessListener { onSuccess(user) }
            .addOnFailureListener { onError(it) }

    }

    override fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val userList = listOf(message.sender, message.receiver)
        val messagePath = userList.sorted().toString()
        database.collection(MESSAGES_COLLECTION).document(messagePath)
            .set(message)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }

    }

}