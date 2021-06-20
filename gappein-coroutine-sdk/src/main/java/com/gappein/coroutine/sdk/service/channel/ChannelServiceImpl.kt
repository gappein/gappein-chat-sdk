package com.gappein.coroutine.sdk.service.channel

import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.Channel
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.util.*
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ChannelServiceImpl : ChannelService {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val channelDatabaseReference: CollectionReference =
        database.collection(CHANNEL_COLLECTION)

    private val userDatabaseReference by lazy {
        database.collection(
            USER_COLLECTION
        )
    }

    companion object {
        private const val CHANNEL_COLLECTION = "channel"

        private const val MESSAGES_COLLECTION = "messages"

        private const val LAST_ONLINE_AT = "lastOnlineAt"

        private const val DELETED = "deleted"

        private const val TRUE = "true"

        private const val IS_ONLINE = "online"

        private const val TOKEN = "token"

        private const val LIKED = "liked"

        private const val NAME = "name"

        private const val IMAGE_URL = "profileImageUrl"

        private const val TYPING = "typing"

        private const val CHANNEL_ID = "channelId"

        private const val CHAT_BACKGROUND = "chat_background"

        private const val USER_COLLECTION = "users"

        private const val ID = "_id"
    }

    override suspend fun sendMessage(message: Message): Boolean {
        return suspendCoroutine<Boolean> { continuation ->
            val userList = listOf(message.sender.token, message.receiver.token)

            val channelId = userList.sorted().toString()

            val channel = channelDatabaseReference.document(channelId)

            val currentChannel = channel.collection(MESSAGES_COLLECTION)
            currentChannel.add(message)
                .addOnSuccessListener { docRef ->
                    updateMessage(
                        currentChannel,
                        channelId,
                        docRef
                    )
                    continuation.resume(true)
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    private fun updateMessage(
        currentChannel: CollectionReference,
        channelId: String,
        documentReference: DocumentReference
    ) {
        currentChannel.document(channelId)
            .updateDocument(Pair(ID, documentReference.id)) { _, _ ->
            }

    }

    override suspend fun getOrCreateNewChatChannels(participantUserToken: String): String {
        val participantUserReference = userDatabaseReference.document(participantUserToken)
        val currentUser = ChatClient.getInstance().getUser()
        val currentUserToken = currentUser.token
        val userList = listOf(participantUserToken, currentUserToken)
        val channelId = userList.sorted().toString()
        val channel: DocumentReference = channelDatabaseReference.document(channelId)
        val result = channel.getValue()
        return suspendCoroutine { continuation ->
            if (!result.exists()) {
                continuation.resume(channelId)
            } else {
                val userMap = HashMap<String, User>()
                getUserByToken(participantUserToken) { user ->
                    userMap[participantUserToken] = user
                    userMap[currentUserToken] = currentUser
                    channelDatabaseReference.document(channelId).set(userMap)
                }
                setupRestForChannel(participantUserReference, participantUserToken, channelId)
            }
        }
    }

    private fun getUserByToken(
        token: String,
        onSuccess: (user: User) -> Unit
    ) {

        userDatabaseReference.get()
            .addOnSuccessListener { result ->
                val user = result.toObject<User>().find {
                    it.getUserWithToken(token)
                }
                user?.let { onSuccess(it) }
            }

    }

    private fun setupRestForChannel(
        participantUserReference: DocumentReference,
        participantUserToken: String,
        channelId: String
    ) {
        val channel = channelDatabaseReference.document(channelId)
        val typingCollection = channel.collection(TYPING)
        val backgroundCollection = channel.collection(CHAT_BACKGROUND)
        val channelMap = mapOf(CHANNEL_ID to channelId)
        val typingMap = mapOf(TYPING to "-")
        val currentUser = ChatClient.getInstance().getUser()
        val currentUserToken = currentUser.token
        val currentUserReference = userDatabaseReference.document(currentUserToken)

        currentUserReference.collection(CHANNEL_COLLECTION)
            .document(participantUserToken)
            .set(channelMap)
        participantUserReference.collection(CHANNEL_COLLECTION)
            .document(currentUserToken)
            .set(channelMap)
        typingCollection.document(participantUserToken)
            .set(typingMap)
        typingCollection.document(currentUserToken)
            .set(typingMap)
        backgroundCollection.document(channelId)
            .set(mapOf(CHAT_BACKGROUND to "-"))


    }

    override suspend fun getUserChannels(): List<Channel> {
        val currentUser = ChatClient.getInstance().getUser()

        val currentUserToken = currentUser.token

        return suspendCoroutine { continuation ->
            channelDatabaseReference.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    val channels = querySnapshot?.documents?.filter {
                        it.id.contains(currentUserToken)
                    }?.map { channel ->
                        val channelMapper: Map<String, User> = channel.data as Map<String, User>
                        val channelUsers = channelMapper.values.toList()
                        return@map Channel(id = channel.id, channelUsers)
                    }
                    continuation.resumeNullable(channels)
                }
            }
        }
    }

    override suspend fun sendMessageByToken(
        message: Message,
        sender: User,
        receiver: User
    ): Boolean {
        return sendMessage(message)
    }

    override suspend fun getMessages(channelId: String): List<Message> {
        val collection = channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
        return suspendCoroutine { continuation ->
            collection.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    val messages = mutableListOf<Message>()
                    val data = querySnapshot?.documents
                        ?.map {
                            return@map it.toObject(Message::class.java)
                        }?.sortedBy {
                            it?.timeStamp
                        } as List<Message>

                    messages.run {
                        clear()
                        addAll(data)
                        continuation.resume(this)
                    }
                }
            }
        }
    }

    override suspend fun getBackupMessages(channelId: String): List<Message> {
        val collection = channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
        return suspendCoroutine { continuation ->
            collection.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    val messages = mutableListOf<Message>()
                    val data = querySnapshot?.documents
                        ?.map {
                            return@map it.toObject(Message::class.java)
                        }?.sortedBy {
                            it?.timeStamp
                        } as List<Message>
                    messages.clear()
                    data.forEach {
                        messages.add(
                            Message(
                                _id = it._id,
                                deleted = it.deleted,
                                isUrl = it.isUrl,
                                liked = it.liked,
                                message = it.message,
                                receiver = it.receiver.copy(token = ""),
                                sender = it.sender.copy(token = ""),
                                timeStamp = it.timeStamp,
                                typing = ""
                            )
                        )
                    }
                    continuation.resume(messages)
                }
            }
        }
    }

    override suspend fun getChannelUsers(channelId: String): List<User> {
        val databaseRef = channelDatabaseReference.document(channelId)
        return suspendCoroutine { continuation ->
            databaseRef.get()
                .addOnSuccessListener {
                    val userData = it.data
                    val userList = userData
                        ?.flatMap { user ->
                            listOf(user.value as HashMap<String, Any>)
                        }?.map { userMap ->
                            return@map User(
                                token = userMap[TOKEN] as String,
                                name = userMap[NAME] as String,
                                profileImageUrl = userMap[IMAGE_URL] as String,
                            )
                        }
                    continuation.resumeNullable(userList)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    }

    override suspend fun getLastMessageFromChannel(channelId: String): Pair<Message, User> {
        val messages = getMessages(channelId)
        return suspendCoroutine { continuation ->
            val users = mutableListOf<User>().apply {
                add(messages.first().receiver)
                add(messages.first().sender)
                filter { user -> user.token == ChatClient.getInstance().getUser().token }
            }
            continuation.resume(Pair(messages.last(), users.first()))
        }
    }

    override suspend fun getChannelRecipientUser(channelId: String): User {
        val document = channelDatabaseReference.document(channelId)
        return suspendCoroutine { continuation ->
            document.get()
                .addOnSuccessListener {
                    val userData = it.data
                    userData
                        ?.flatMap { user ->
                            listOf(user.value as HashMap<String, Any>)
                        }?.map { userMap ->
                            return@map User(
                                token = userMap[TOKEN] as String,
                                name = userMap[NAME] as String,
                                profileImageUrl = userMap[IMAGE_URL] as String,
                            )
                        }?.forEach { user ->
                            if (user.token != ChatClient.getInstance().getUser().token) {
                                continuation.resume(user)
                            }
                        }
                }
        }
    }

    override suspend fun getAllChannels(): List<Channel> {
        return suspendCoroutine { continuation ->
            channelDatabaseReference.addSnapshotListener { querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val channels = querySnapshot
                    ?.documents
                    ?.map { channel ->
                        val channelMapper: Map<String, User> = channel.data as Map<String, User>
                        val channelUsers = channelMapper.values.toList()
                        return@map Channel(id = channel.id, channelUsers)
                    }
                if (channels != null) {
                    continuation.resume(channels)
                }
            }
        }
    }

    override suspend fun deleteMessage(channelId: String, message: Message): Boolean {
        return suspendCoroutine { continuation ->
            if (message.sender.isCurrentUser()) {
                channelDatabaseReference.document(channelId)
                    .collection(MESSAGES_COLLECTION)
                    .document(message._id)
                    .update(DELETED, true)
                    .addOnSuccessListener { continuation.resume(true) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            } else {
                continuation.resumeWithException(Exception("You don't have permission to delete this message"))
            }
        }
    }

    override suspend fun setTypingStatus(channelId: String, userId: String, isUserTyping: Boolean) {
        val currentUser = ChatClient.getInstance().getUser()
        updateTypingStatus(currentUser, isUserTyping, channelId)
    }

    private fun updateTypingStatus(currentUser: User, isUserTyping: Boolean, channelId: String) {
        val userCurrentReference = channelDatabaseReference.document(channelId)
            .collection(TYPING)
            .document(currentUser.token)
        if (isUserTyping) {
            userCurrentReference
                .update(TYPING, "${currentUser.name.capitalize(Locale.ROOT)} is typing..")
        } else {
            userCurrentReference
                .update(TYPING, "-")
        }
    }

    override suspend fun getTypingStatus(channelId: String, participantUserId: String): String {
        val document = channelDatabaseReference.document(channelId)
            .collection(TYPING)
            .document(participantUserId)
        return suspendCoroutine { continuation ->
            document.addSnapshotListener { response, exception ->
                if (exception != null) {
                    continuation.resumeWithExceptionNullable(exception)
                } else {
                    continuation.resumeNullable(response?.get(TYPING).toString())
                }
            }
        }
    }

    override suspend fun setChatBackground(channelId: String, backgroundUrl: String) {
        val document = channelDatabaseReference.document(channelId)
            .collection(CHAT_BACKGROUND)
            .document(channelId)
        return suspendCoroutine { continuation ->
            document.update(CHAT_BACKGROUND, backgroundUrl)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithExceptionNullable(it) }
        }
    }

    override suspend fun getChatBackground(channelId: String): String {
        val document = channelDatabaseReference.document(channelId)
            .collection(CHAT_BACKGROUND)
            .document(channelId)
        return suspendCoroutine { continuation ->
            document.addSnapshotListener { response, exception ->
                if (exception != null) {
                    continuation.resumeWithExceptionNullable(exception)
                } else {
                    continuation.resumeNullable(response?.data?.get(CHAT_BACKGROUND).toString())
                }
            }
        }
    }

    override suspend fun likeMessage(channelId: String, messageId: String) {
        val collection = channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
        val isSuccessful = collection.document(messageId).updateDocument(Pair(LIKED, true))
        return suspendCoroutine { continuation ->
            if (isSuccessful.first) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithExceptionNullable(isSuccessful.second)
            }
        }
    }

    override suspend fun isUserOnline(token: String): Pair<Boolean, String> {
        val userChannelReference = userDatabaseReference.document(token)
        return suspendCoroutine { continuation ->
            userChannelReference.addSnapshotListener { value, _ ->
                if (value != null && value.data != null) {
                    val userData = value.data as Map<String, User>
                    if (userData[IS_ONLINE].toString() == TRUE) {
                        continuation.resume(Pair(true, ""))
                    } else {
                        continuation.resume(Pair(false, userData[LAST_ONLINE_AT].toString()))
                    }
                }
            }
        }
    }
}


