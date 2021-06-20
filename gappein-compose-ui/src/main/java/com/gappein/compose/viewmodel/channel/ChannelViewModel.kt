package com.gappein.compose.viewmodel.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChannelViewModel(private val chatClient: ChatClient) : ViewModel() {

    private val _messageList = MutableStateFlow<List<Channel>>(emptyList())
    val messageList: StateFlow<List<Channel>>
        get() = _messageList

    fun fetchChannel() {
        viewModelScope.launch {
            _messageList.value = chatClient.getUserChannels()
        }
    }
}