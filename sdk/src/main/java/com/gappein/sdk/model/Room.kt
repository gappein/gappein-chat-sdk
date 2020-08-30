package com.gappein.sdk.model

data class Room(
    val name: String,
    val id: String,
    val lastMessage: String,
    val lastUpdated: String,
    var isLastRead: Boolean,
    val sender: User,
    val receiver: User
)