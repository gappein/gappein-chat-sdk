package com.gappein.sdk.impl

import com.gappein.sdk.client.ChannelClient
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager

class ChannelClientImpl(private val dbManager: FirebaseDbManager) : ChannelClient {

    override fun setUsers(receiver: User, sender: User) {

    }

    override fun getChannels(): List<Channel> {
        return dbManager.getChannels(ChatClient.instance().getUser()) {

        }
    }

}