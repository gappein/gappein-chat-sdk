package com.gappein.sdk.data.db

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


@Suppress("UNCHECKED_CAST")
class FirebaseDbManagerImpl : FirebaseDbManager {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val channelReference = database.collection(CHANNEL_COLLECTION)
    private val userReference = database.collection(USER_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val MESSAGES_COLLECTION = "messages"
        private const val CHANNEL_COLLECTION = "channel"
        private const val CHANNEL_ID = "channelId"
        private const val TOKEN = "token"
        private const val NAME = "name"
        private const val IMAGE_URL = "profileImageUrl"
        private const val IS_ONLINE = "isOnline"
        private const val DELETED = "deleted"
        private const val LAST_ONLINE_AT = "lastOnlineAt"
    }

    override fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {

        val reference = userReference.document(user.token)

        reference.get()
            .addOnSuccessListener { _user ->
                if (!_user.exists()) {
                    reference.set(user)
                        .addOnSuccessListener { onSuccess(user) }
                        .addOnFailureListener { onError(it) }
                } else {
                    onSuccess(user)
                }
            }
    }

    override fun sendMessage(
        message: Message,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val userList = listOf(message.sender.token, message.receiver.token)
        val channelId = userList.sorted().toString()

        channelReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .add(message)
            .addOnSuccessListener {
                updateMessage(channelId, it, onSuccess, onError)
            }
            .addOnFailureListener { onError(it) }
    }

    private fun updateMessage(
        channelId: String,
        it: DocumentReference,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        channelReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .document(it.id)
            .update("_id", it.id)
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
                val user = data.find { it.token == token }
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

    override fun getOrCreateNewChatChannels(
        participantUserToken: String,
        onSuccess: (channelId: String) -> Unit
    ) {

        val userChannelReference = channelReference.document(participantUserToken)
        val currentUser = ChatClient.getInstance().getUser()
        val currentUserToken = currentUser.token
        val currentUserReference = userReference.document(currentUserToken)
        val participantUserReference = userReference.document(participantUserToken)

        val userList = listOf(participantUserToken, currentUserToken)
        val channelId = userList.sorted().toString()

        userChannelReference.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    onSuccess(it[CHANNEL_ID] as String)
                    return@addOnSuccessListener
                }
                val userMap = HashMap<String, User>()
                getUserByToken(participantUserToken, { user ->
                    userMap[participantUserToken] = user
                    userMap[currentUserToken] = currentUser
                    channelReference.document(channelId).set(userMap)
                }, {

                })
                addChannelsToUser(currentUserReference, participantUserToken, channelId)

                addChannelsToUser(participantUserReference, currentUserToken, channelId)
                onSuccess(channelId)
            }

    }


    private fun addChannelsToUser(reference: DocumentReference, token: String, messageId: String) {
        reference
            .collection(CHANNEL_COLLECTION)
            .document(token)
            .set(mapOf(CHANNEL_ID to messageId))
    }

    override fun getUserChannels(onSuccess: (List<Channel>) -> Unit) {

        val currentUserToken = ChatClient.getInstance().getUser().token

        channelReference.addSnapshotListener { querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
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
        channelReference.document(channelId)
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
                getChannelUsers(channelId) { _u ->
                    val user =
                        _u.filter { user -> user.token != ChatClient.getInstance().getUser().token }
                    onSuccess(Message(), user.first())
                }
            }
        }

    }

    override fun getChannelUsers(channelId: String, onSuccess: (List<User>) -> Unit) {
        channelReference.document(channelId)
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

    override fun getChannelRecipientUser(channelId: String, onSuccess: (User) -> Unit) {
        channelReference.document(channelId)
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

    override fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit) {
        val userChannelReference = userReference.document(token)
        userChannelReference.get()
            .addOnSuccessListener {
                if (it.data != null) {
                    val userData = it.data as Map<String, User>
                    if (userData[IS_ONLINE].toString() == "true") {
                        onSuccess(true, "")
                    } else {
                        onSuccess(false, userData[LAST_ONLINE_AT].toString())
                    }
                }
            }
    }

    override fun setUserOnline(token: String) {
        val userChannelReference = userReference.document(token)
        userChannelReference.update(IS_ONLINE, true)
            .addOnSuccessListener {

            }
    }

    override fun getAllChannels(onSuccess: (List<Channel>) -> Unit) {

        channelReference.addSnapshotListener { querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
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

    override fun deleteMessage(channelId: String, messageId: String, onSuccess: () -> Unit) {
        channelReference.document(channelId)
            .collection(MESSAGES_COLLECTION)
            .document(messageId)
            .update(DELETED, true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { }
    }

    override fun setTypingStatus(
        channelId: String,
        userId: String,
        status: Boolean,
        onSuccess: () -> Unit
    ) {
        channelReference.document(channelId)
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
                val currentUser = userList?.filter { it.token == userId }?.get(0)
                updateTypingStatus(currentUser, status, channelId)
            }
    }

    private fun updateTypingStatus(currentUser: User?, isUserTyping: Boolean, channelId: String) {
        if (isUserTyping) {
            channelReference.document(channelId)
                .update("${currentUser?.token}.typing", "${currentUser?.name} is typing..")
        } else {
            channelReference.document(channelId)
                .update("${currentUser?.token}.typing", "-")
        }
    }

    //for later implementation will check soon.
    private fun getAllUsers(onSuccess: (List<User>) -> Unit) {
        userReference.addSnapshotListener { value, _ ->
            val userList = mutableListOf<User>()
            value?.documents?.map {
                val userMapper: Map<String, User> = it.data as Map<String, User>
                userList.addAll(userMapper.values.toList())
            }
            onSuccess(userList)
        }
    }
}