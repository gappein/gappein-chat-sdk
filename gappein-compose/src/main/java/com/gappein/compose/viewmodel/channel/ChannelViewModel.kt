package com.gappein.compose.viewmodel.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.ChannelListData
import com.gappein.sdk.util.toChannelList

/**
 * Created by Himanshu Singh on 09-03-2021.
 * hello2himanshusingh@gmail.com
 */
class ChannelViewModel(private val chatClient: ChatClient) : ViewModel() {

    private val _userChannels = MutableLiveData<List<ChannelListData>>(emptyList())
    val userChannels: LiveData<List<ChannelListData>>
        get() = _userChannels

    private val channelListItem = mutableListOf<ChannelListData>()

    fun init() {
        fetchChannels()
    }


    private fun fetchChannels() {
        chatClient.getUserChannels { channels ->
            channels.forEach {
                it.toChannelList(chatClient) {
                    channelListItem.add(it)
                }
            }
            _userChannels.postValue(channelListItem)
        }
    }

}