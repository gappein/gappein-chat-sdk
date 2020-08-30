package com.gappein.sdk.listener

import com.gappein.sdk.model.User


open class InitConnectionListener {

    open fun onSuccess(data: ConnectionData) {
    }

    open fun onError(error: String) {
    }

    data class ConnectionData(val user: User, val token: String)
}