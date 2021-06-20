package com.gappein.sdk.service.channel

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.gappein.sdk.util.getObject
import com.gappein.sdk.util.getValue
import com.gappein.sdk.util.updateDocument
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
class ChannelServiceImpl : ChannelService {

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

        private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

        private val channelDatabaseReference: CollectionReference = database.collection(
            CHANNEL_COLLECTION
        )

        private val userDatabaseReference by lazy {
            database.collection(
                USER_COLLECTION
            )
        }

    }

    override fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userList = listOf(message.sender.token, message.receiver.token)

        val channelId = userList.sorted().toString()

        val channel = channelDatabaseReference.document(channelId)

        val currentChannel = channel.collection(MESSAGES_COLLECTION)
        currentChannel
            .add(message)
            .addOnSuccessListener {
                updateMessage(
                    currentChannel,
                    channelId,
                    it,
                    onSuccess,
                    onError
                )
            }
            .addOnFailureListener { onError(it) }

    }

    private fun updateMessage(
        currentChannel: CollectionReference,
        channelId: String,
        documentReference: DocumentReference,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        currentChannel.document(channelId)
            .updateDocument(Pair(ID, documentReference.id)) { isSuccessful, exception ->
                if (isSuccessful) {
                    onSuccess()
                } else {
                    exception?.let { onError(it) }
                }
            }
    }

    override fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onSuccess: (channelId: String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val participantUserReference = userDatabaseReference.document(participantUserToken)
        val currentUser = ChatClient.getInstance().getUser()

        val currentUserToken = currentUser.token
        val userList = listOf(participantUserToken, currentUserToken)

        val channelId = userList.sorted().toString()

        val channel = channelDatabaseReference.document(channelId)

        channel.getValue({ _channel ->
            if (!_channel.exists()) {
                onSuccess(channelId)
            } else {
                val userMap = HashMap<String, User>()
                getUserByToken(participantUserToken) { user ->
                    userMap[participantUserToken] = user
                    userMap[currentUserToken] = currentUser
                    channelDatabaseReference.document(channelId).set(userMap)
                }
                setupRestForChannel(participantUserReference, participantUserToken, channelId)
                onSuccess(channelId)
            }
        }, {
            onError(it)
        })
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

    override fun getUserChannels(onSuccess: (List<Channel>) -> Unit) {
        val currentUser = ChatClient.getInstance().getUser()

        val currentUserToken = currentUser.token
        channelDatabaseReference.addSnapshotListener { querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (error != null) {
                return@addSnapshotListener
            }

            val channels = querySnapshot
                ?.documents
                ?.filter {
                    it.id.contains(currentUserToken)
                }?.map { channel ->
                    val channelMapper: Map<String, User> = channel.data as Map<String, User>
                    val channelUsers = channelMapper.values.toList()
                    return@map Channel(id = channel.id, channelUsers)
                }
            channels?.let { onSuccess(it) }
        }
    }

    override fun sendMessageByToken(
        message: Message,
        sender: User,
        receiver: User,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        sendMessage(message, onSuccess, onError)
    }

    override fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit) {
        channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
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
                    onSuccess(this)
                }
            }
    }

    override fun getBackupMessages(channelId: String, onSuccess: (List<Message>) -> Unit) {
        channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
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
                onSuccess(messages)
            }
    }

    override fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit) {
        channelDatabaseReference.document(channelId)
            .get()
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
                userList?.let { users -> onSuccess(users) }
            }
    }

    override fun getLastMessageFromChannel(channelId: String, onSuccess: (Message, User) -> Unit) {
        getMessages(channelId) {
            if (it.isNotEmpty()) {
                val users = mutableListOf<User>().apply {
                    add(it.first().receiver)
                    add(it.first().sender)
                    filter { user -> user.token == ChatClient.getInstance().getUser().token }
                }
                onSuccess(it.last(), users.first())
            } else {
                getChannelUsers(channelId) { userList ->
                    val user = userList.filter { user ->
                        user.token != ChatClient.getInstance().getUser().token
                    }
                    onSuccess(Message(), user.first())
                }
            }
        }
    }

    override fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit) {
        channelDatabaseReference.document(channelId)
            .get()
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
                            onSuccess(user)
                        }
                    }
            }
    }

    override fun getAllChannels(onSuccess: (List<Channel>) -> Unit) {

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
            channels?.let { onSuccess(it) }
        }
    }

    override fun deleteMessage(
        channelId: String,
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (message.sender.isCurrentUser()) {
            channelDatabaseReference.document(channelId)
                .collection(MESSAGES_COLLECTION)
                .document(message._id)
                .update(DELETED, true)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onError(it) }
        } else {
            onError(Exception("You don't have permission to delete this message"))
        }
    }

    override fun setTypingStatus(
        channelId: String,
        userId: String,
        isUserTyping: Boolean,
        onSuccess: () -> Unit
    ) {
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

    override fun getTypingStatus(
        channelId: String,
        participantUserId: String,
        onSuccess: (String) -> Unit
    ) {
        channelDatabaseReference.document(channelId)
            .collection(TYPING)
            .document(participantUserId)
            .addSnapshotListener { value, _ ->
                onSuccess(value?.get(TYPING).toString())
            }
    }

    override fun setChatBackground(
        channelId: String,
        backgroundUrl: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        channelDatabaseReference.document(channelId)
            .collection(CHAT_BACKGROUND)
            .document(channelId)
            .update(CHAT_BACKGROUND, backgroundUrl)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    override fun getChatBackground(channelId: String, onSuccess: (String) -> Unit) {
        channelDatabaseReference.document(channelId)
            .collection(CHAT_BACKGROUND)
            .document(channelId)
            .addSnapshotListener { value, _ ->
                onSuccess(value?.data?.get(CHAT_BACKGROUND).toString())
            }
    }

    override fun likeMessage(
        channelId: String,
        messageId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        channelDatabaseReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .document(messageId)
            .updateDocument(Pair(LIKED, true)) { isSuccessful, exception ->
                if (isSuccessful) {
                    onSuccess()
                } else {
                    exception?.let { onError(it) }
                }
            }
    }

    private fun getUserByToken(
        token: String,
        onSuccess: (user: User) -> Unit
    ) {

        userDatabaseReference.get()
            .addOnSuccessListener { result ->
                val user = result.getObject<User>().find {
                    it.getUser(token)
                }
                user?.let { onSuccess(it) }
            }

    }

    override fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit) {
        val userChannelReference = userDatabaseReference.document(token)
        userChannelReference.addSnapshotListener { value, _ ->
            if (value != null && value.data != null) {
                val userData = value.data as Map<String, User>
                if (userData[IS_ONLINE].toString() == TRUE) {
                    onSuccess(true, "")
                } else {
                    onSuccess(false, userData[LAST_ONLINE_AT].toString())
                }
            }
        }
    }

}