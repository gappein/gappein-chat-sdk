package com.gappein.sdk.model

import android.os.Parcelable
import com.gappein.sdk.client.ChatClient
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(

    val token: String = "",

    var createdAt: Date? = null,

    var lastOnlineAt: Date? = null,

    val profileImageUrl: String = "",

    val name: String = "",

    var isOnline: Boolean = false,

    var isTyping: Boolean = false,

) : Parcelable

fun User.isCurrentUser():Boolean{
    return this.token == ChatClient.getInstance().getUser().token
}