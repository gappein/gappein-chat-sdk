package com.gappein.sdk.model

import com.gappein.sdk.client.ChatClient


data class Message(

    var timeStamp: Long? = System.currentTimeMillis(),

    val _id: String = "",

    val message: String = "",

    val sender: User = User(),

    val receiver: User = User(),

    var isUrl: Boolean = false,

    val deleted: Boolean = false,

    val typing: String = "",

    val liked: Boolean = false
)
fun User.isCurrentUser(): Boolean {
        return this.token == ChatClient.getInstance().getUser().token
}

