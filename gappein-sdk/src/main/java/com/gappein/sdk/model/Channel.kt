package com.gappein.sdk.model

data class Channel(

    var id: String = "",

    val users: List<User> = arrayListOf()

)

