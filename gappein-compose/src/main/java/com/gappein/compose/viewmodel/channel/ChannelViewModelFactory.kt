package com.gappein.compose.viewmodel.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gappein.sdk.client.ChatClient

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
@Suppress("UNCHECKED_CAST")
class ChannelViewModelFactory(private val chatClient: ChatClient) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelViewModel::class.java)) {
            return ChannelViewModel(chatClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}