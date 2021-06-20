package com.gappein.coroutine.sdk.service.channel

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class OIChannellServiceImpll : ChannelService {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val channelDatabaseReference: CollectionReference = database.collection(
        CHANNEL_COLLECTION
    )

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
}