package com.gappein.compose.components.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gappein.compose.viewmodel.channel.ChannelViewModel
import com.gappein.compose.viewmodel.channel.ChannelViewModelFactory
import com.gappein.sdk.client.ChatClient

/**
 * Created by Himanshu Singh on 01-04-2021.
 * hello2himanshusingh@gmail.com
 */
@Composable
fun ChannelList(
    channelViewModel: ChannelViewModel = viewModel(
        factory = ChannelViewModelFactory(ChatClient.getInstance())
    )
) {
    channelViewModel.init()
    val state by channelViewModel.userChannels.observeAsState()

    val userChannels = state ?: emptyList()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (userChannels.isEmpty()) {
            TextButton(onClick = { }) {
                Text(text = "Create Chat.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
                items(items = userChannels) { channel ->
                    ChannelListItem(channel)
                    Divider()
                }
            }
        }
    }
}