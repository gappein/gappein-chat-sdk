package com.gappein.compose.viewmodel.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gappein.sdk.client.ChatClient

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory (private val chatClient: ChatClient) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}