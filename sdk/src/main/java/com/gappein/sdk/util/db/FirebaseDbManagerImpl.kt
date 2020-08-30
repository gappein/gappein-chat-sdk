package com.gappein.sdk.util.db

import android.util.Log
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.ChatChanel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class FirebaseDbManagerImpl : FirebaseDbManager {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val channelReference = database.collection(CHANNEL_COLLECTION)
    private val userReference = database.collection(USER_COLLECTION)

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
        val channelId = userList.sorted().toString()
        Log.d("SDfsf",channelId)
        channelReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .add(message)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
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
        val currentUser = ChatClient.instance().getUser()
        val currentUserToken = currentUser.token
        val currentUserReference = userReference.document(currentUserToken)
        val participantUserReference = userReference.document(participantUserToken)
        val userList = listOf(participantUserToken, currentUserToken)
        val messageId = userList.sorted().toString()

        userChannelReference.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it[CHANNEL_ID] as String)
                    return@addOnSuccessListener
                }
                getUserByToken(participantUserToken, {
                    channelReference.document(messageId)
                        .set(ChatChanel(currentUser, it))

                }, {

                })
//
                addChannelsToUser(currentUserReference, participantUserToken, messageId)

                addChannelsToUser(participantUserReference, currentUserToken, messageId)
            }
    }


    private fun addChannelsToUser(reference: DocumentReference, token: String, messageId: String) {
        reference
            .collection(CHANNEL_COLLECTION)
            .document(token)
            .set(mapOf(CHANNEL_ID to messageId))
    }

    override fun getAllChannel(onSuccess: (List<String>) -> Unit) {
        val currentUserId = ChatClient.instance().getUser().token
        val result = mutableListOf<String>()
        channelReference.addSnapshotListener { querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (error != null) {
                return@addSnapshotListener
            }
            querySnapshot?.documents?.forEach {
                if (it.id.contains(currentUserId)) {
                    result.add(it.id)
                }
            }
            onSuccess(result)
        }
    }
}