package com.gappein.compose.component.channel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gappein.compose.viewmodel.channel.ChannelViewModel
import com.gappein.compose.viewmodel.channel.ChannelViewModelFactory

@Composable
fun ChannelList(channelViewModel: ChannelViewModel = viewModel(factory = ChannelViewModelFactory())) {
}