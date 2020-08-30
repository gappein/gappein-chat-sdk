package com.gappein.sdk.client

import com.gappein.sdk.impl.ChannelClientImpl
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.db.FirebaseDbManagerImpl

interface ChannelClient {

    companion object {

        private var instance: ChannelClient? = null

        @JvmStatic
        fun instance(): ChannelClient {
            return instance
                ?: throw IllegalStateException("Gappein.Builder::build() must be called before obtaining Gappein instance")
        }
    }

    class Builder {
        private val dbManager: FirebaseDbManager by lazy { FirebaseDbManagerImpl() }

        fun build(): ChannelClient {
            return ChannelClientImpl(dbManager).apply {
                instance = this
            }
        }
    }

    fun setUsers(receiver: User, sender: User)

    fun getChannels(): List<Channel>

}