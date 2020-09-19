package com.gappein.sdk.model

import java.util.*

data class Message(

        var timeStamp: Long? = System.currentTimeMillis(),

        val message: String = "",

        val sender: User = User(),

        val receiver: User = User(),

        var isUrl: Boolean = false
)

