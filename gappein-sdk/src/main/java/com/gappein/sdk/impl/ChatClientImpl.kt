package com.gappein.sdk.impl

import android.net.Uri
import android.webkit.URLUtil
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.storage.FirebaseStorageManager
import java.util.*

class ChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: FirebaseDbManager
) : ChatClient {

    private var currentUser = User()

    override fun setUser(user: User, token: String, listener: InitConnectionListener?) {
        currentUser = (user)
        dbManager.createUser(user, {
            listener?.onSuccess(InitConnectionListener.ConnectionData(it, token))
        }, {
            listener?.onError(it.localizedMessage ?: "User already present")
        })
    }

    override fun getUser() = currentUser

    override fun sendMessage(
        message: String,
        receiver: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        getUserByToken(receiver, {

            val _message = Message(
                timeStamp = Calendar.getInstance().time,
                message = message,
                isUrl = URLUtil.isValidUrl(message),
                receiver = it,
                sender = getUser()
            )

            dbManager.sendMessageByToken(_message, getUser(), it, onSuccess, onError)

        }, {

        })

    }

    override fun sendMessage(
        fileUri: Uri,
        receiver: User,
        onSuccess: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        storageManager.uploadMessageImage(fileUri, receiver, getUser(), {
            sendMessage(it, receiver.token, {}, {})
        }, {
            onProgress(it)
        }, {
            onError(it)
        })
    }

    override fun getUserByToken(
        token: String,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        dbManager.getUserByToken(token, onSuccess, onError)
    }

    override fun openOrCreateChannel(
        participantUserToken: String,
        onComplete: (channelId: String) -> Unit
    ) {
        dbManager.getOrCreateNewChatChannels(participantUserToken, onComplete)
    }

    override fun getAllChannels(onSuccess: (List<String>) -> Unit) {
        dbManager.getAllChannel(onSuccess)
    }
}