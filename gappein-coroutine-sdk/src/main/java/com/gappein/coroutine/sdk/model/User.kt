package com.gappein.coroutine.sdk.model

import android.os.Parcelable
import com.gappein.coroutine.sdk.client.ChatClient
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(

    val token: String = "",

    val createdAt: Date? = null,

    val profileImageUrl: String = "",

    val name: String = "",

    val isOnline: Boolean = false,

    val textStatus: String = ""
) : Parcelable {

    suspend fun isCurrentUser(): Boolean {
        return this.token == ChatClient.getInstance().getUser().token
    }

    fun getUserWithToken(token: String): Boolean {
        return this.token == token
    }

}