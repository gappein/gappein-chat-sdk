package com.gappein.sdk.model

data class Channel(
    val name: String,
    val id: String,
    val lastMessage: String,
    val lastUpdated: String,
    var isLastRead: Boolean,
    val sender: User,
    val receiver: User
)
data class ChatChanel(val userId: MutableList<String>) {
    constructor():this(mutableListOf())
}