package com.gappein.coroutine.sdk.model

data class ChannelListData(

    var id: String,

    val user: User,

    val lastMessage: Message,

    var isUserOnline: Boolean = false

)

