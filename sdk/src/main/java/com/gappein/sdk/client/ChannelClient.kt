package com.gappein.sdk.client

import com.gappein.sdk.impl.ChannelClientImpl
import com.gappein.sdk.model.User

interface ChannelClient {

    companion object {

        private var instance: ChannelClient? = null

        @JvmStatic
        fun instance(): ChannelClient {
            return instance
                ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")
        }
    }

    class Builder(
        private val receiver: User,
        private val sender: User
    ) {

        fun build(): ChannelClient {
            return ChannelClientImpl(
                receiver,
                sender
            ).apply {
                instance = this
            }
        }
    }

    fun setUsers(receiver: User, sender: User)

}