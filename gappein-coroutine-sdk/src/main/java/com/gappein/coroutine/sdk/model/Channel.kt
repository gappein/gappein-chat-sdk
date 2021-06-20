package com.gappein.coroutine.sdk.model

data class Channel(

    var id: String = "",

    val users: List<User> = arrayListOf()

)

