package com.gappein.sdk.model

import java.util.*

data class User(

    val token: String = "",

    var createdAt: Date? = null,

    val profileImageUrl: String = "",

    val name: String = ""
)