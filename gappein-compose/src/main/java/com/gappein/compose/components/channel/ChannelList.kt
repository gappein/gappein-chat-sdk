package com.gappein.compose.components.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gappein.compose.viewmodel.channel.ChannelViewModel
import com.gappein.sdk.client.ChatClient

/**
 * Created by Himanshu Singh on 01-04-2021.
 * hello2himanshusingh@gmail.com
 */
@Composable
fun ChannelList(
    channelViewModel: ChannelViewModel
) {
    val state by channelViewModel.userChannels.observeAsState()
    val userChannels = state ?: emptyList()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (userChannels.isEmpty()) {
            return
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(items = userChannels) { channel ->
                    ChannelListItem(channel)
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun Hey() {
    ChannelList(channelViewModel = ChannelViewModel(ChatClient.getInstance()))
}