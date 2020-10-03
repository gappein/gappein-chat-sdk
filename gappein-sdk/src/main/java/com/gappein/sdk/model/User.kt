package com.gappein.sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(

    val token: String = "",

    val createdAt: Date? = null,

    val profileImageUrl: String = "",

    val name: String = "",

    val isOnline: Boolean = false,

    val textStatus: String = ""
) : Parcelable