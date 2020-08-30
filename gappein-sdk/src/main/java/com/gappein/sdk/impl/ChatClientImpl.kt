package com.gappein.sdk.impl

import android.net.Uri
import android.webkit.URLUtil
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.gappein.sdk.data.db.FirebaseDbManager
import com.gappein.sdk.data.storage.FirebaseStorageManager
import java.util.*

class ChatClientImpl(private val storageManager: FirebaseStorageManager, private val dbManager: FirebaseDbManager) : ChatClient {

    private var currentUser = User()

    override fun setUser(user: User, token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {
        dbManager.createUser(user, {
            currentUser = (user)
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override fun getUser() = currentUser

    override fun sendMessage(messageText: String, receiver: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        getUserByToken(receiver, {

            val message = Message(
                timeStamp = Calendar.getInstance().time,
                message = messageText,
                isUrl = URLUtil.isValidUrl(messageText),
                receiver = it,
                sender = getUser()
            )

            dbManager.sendMessageByToken(message, getUser(), it, onSuccess, onError)

        }, {
            onError(it)
        })
    }

    override fun sendMessage(fileUri: Uri, receiver: String, onSuccess: () -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit) {

        getUserByToken(receiver, {
            storageManager.uploadMessageImage(fileUri, it, getUser(), { message ->
                sendMessage(message, receiver, {

                }, {
                    onError(it)
                })
            }, { progress ->
                onProgress(progress)
            }, { ex ->
                onError(ex)
            })
        }, {
            onError(it)
        })

    }

    override fun getUserByToken(token: String, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {
        dbManager.getUserByToken(token, onSuccess, onError)
    }

    override fun openOrCreateChannel(participantUserToken: String, onComplete: (channelId: String) -> Unit) {
        dbManager.getOrCreateNewChatChannels(participantUserToken, onComplete)
    }

    override fun getAllChannels(onSuccess: (List<String>) -> Unit) {
        dbManager.getAllChannel(onSuccess)
    }

    override fun getMessages(channelId: String, onSuccess: (List<Message>) -> Unit) {
        dbManager.getMessages(channelId,onSuccess)
    }
}