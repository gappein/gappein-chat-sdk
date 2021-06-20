package com.gappein.compose.viewmodel.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gappein.coroutine.sdk.client.ChatClient

@Suppress("UNCHECKED_CAST")
class MessageViewModelFactory(private val chatClient: ChatClient) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(chatClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}