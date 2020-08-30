package com.gappein.sdk.model

import java.util.*

data class Message(
    private val timeStamp: Date = Calendar.getInstance().time,
    val message: String = "",
    val sender: String,
    val receiver: String,
    val isUrl: Boolean = false
)