package com.gappein.sdk.util.db

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.ChatChanel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDbManagerImpl : FirebaseDbManager {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val channelReference = database.collection(CHANNEL_COLLECTION)
    private val userReference = database.collection(USER_COLLECTION)
    private val messageReference = database.collection(MESSAGES_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val MESSAGES_COLLECTION = "messages"
        private const val CHAT_COLLECTION = "chat"
        private const val CHANNEL_COLLECTION = "channel"
        private const val CHANNEL_ID = "channelId"
    }

    override fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {

        val reference = userReference.document(user.token)

        reference.get()
            .addOnSuccessListener { _user ->
                if (!_user.exists()) {
                    reference.set(user)
                        .addOnSuccessListener { onSuccess(user) }
                        .addOnFailureListener { onError(it) }
                }
            }

    }

    override fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val userList = listOf(message.sender, message.receiver)
        val messagePath = userList.sorted().toString()

        messageReference
            .document(messagePath)
            .collection(CHAT_COLLECTION)
            .document()
            .set(message)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }

    }

    private fun addReceiver(
        receiver: String,
        sender: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        database.collection(sender)
            .document(receiver)
            .set(receiver)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    override fun getChannels(user: User, onError: (Exception) -> Unit): List<Channel> {
        return emptyList()
    }

    override fun getUserByToken(
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        userReference
            .get()
            .addOnSuccessListener { result ->
                val data = result.toObjects(User::class.java)
                val user = data.find {
                    it.token == token
                }
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onComplete: (channelId: String) -> Unit
    ) {
        val userChannelReference = channelReference.document(participantUserToken)
        val myToken = ChatClient.instance().getUser().token
        val currentUserReference = userReference.document(ChatClient.instance().getUser().token)
        val userReference = userReference.document(participantUserToken)
        val userList = listOf(participantUserToken, myToken)
        val messagePath = userList.sorted().toString()

        userChannelReference.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it[CHANNEL_ID] as String)
                    return@addOnSuccessListener
                }
                val currentUserToken = ChatClient.instance().getUser().token
                val newChannel = channelReference.document(messagePath)
                newChannel.set(ChatChanel(mutableListOf(currentUserToken, participantUserToken)))

                currentUserReference
                    .collection(CHANNEL_COLLECTION)
                    .document(participantUserToken)
                    .set(mapOf(CHANNEL_ID to messagePath))

                userReference
                    .collection(CHANNEL_COLLECTION)
                    .document(currentUserToken)
                    .set(mapOf(CHANNEL_ID to messagePath))
                onComplete(messagePath)
            }
    }
}