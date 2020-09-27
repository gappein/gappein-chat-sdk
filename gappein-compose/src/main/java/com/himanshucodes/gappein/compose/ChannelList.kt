package com.himanshucodes.gappein.compose

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gappein.sdk.client.ChatClient

@Composable
fun ChannelList() {
    ChatClient.getInstance().getAllChannels { channel ->
        LazyColumnFor(channel, modifier = Modifier) {

        }
    }
}