package com.gappein.sdk.model

data class ChannelListData(

    var id: String = "",

    val users: User,

    val lastMessage: Message
)

