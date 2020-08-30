package com.gappein.sdk.model

import java.util.*

data class Message(
        var timeStamp: Date = Calendar.getInstance().time,
        val message: String = "",
        val sender: User,
        val receiver: User,
        var isUrl: Boolean = false
)