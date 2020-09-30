package com.gappein.sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(

    var token: String = "",

    var createdAt: Date? = null,

    var profileImageUrl: String = "",

    var name: String = "",

    var isOnline: Boolean = false,

    var typing:String = ""
) : Parcelable