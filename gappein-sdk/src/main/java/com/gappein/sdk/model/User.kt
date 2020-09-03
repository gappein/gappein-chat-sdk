package com.gappein.sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(

    val token: String = "",

    var createdAt: Date? = null,

    val profileImageUrl: String = "",

    val name: String = "",

    var isOnline: Boolean = false
) : Parcelable