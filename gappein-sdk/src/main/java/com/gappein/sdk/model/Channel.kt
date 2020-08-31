package com.gappein.sdk.model

data class Channel(

    val id: String,

    val lastMessage: String,

    val lastUpdated: String,

    var isLastRead: Boolean,

    val sender: User,

    val receiver: User
)

