package com.gappein.sdk.model


data class Message(

        var timeStamp: Long? = System.currentTimeMillis(),

        val _id:String ="",

        val message: String = "",

        val sender: User = User(),

        val receiver: User = User(),

        var isUrl: Boolean = false,

        val deleted:Boolean = false,

        val typing:String = ""
)

