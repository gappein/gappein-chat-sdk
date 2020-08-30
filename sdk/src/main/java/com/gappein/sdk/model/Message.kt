package com.gappein.sdk.model

import java.util.*

data class Message(
    var timeStamp: Date = Calendar.getInstance().time,
    val message: String = "",
    val sender: String,
    val receiver: String,
    var isUrl: Boolean = false
)