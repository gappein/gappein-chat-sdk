package com.gappein.sdk.ui.view.channelview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.view.channelview.adapter.ChannelListAdapter
import kotlinx.android.synthetic.main.fragment_channel_list.view.*


class ChannelListFragment : Fragment(), ChatBaseView {

    private lateinit var adapter: ChannelListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChannelList(view)
        fetchChannels()
    }

    private fun fetchChannels() {
        getClient().getUserChannels {
            adapter.addAll(it)
        }
    }

    private fun setupChannelList(view: View) {
        adapter = ChannelListAdapter()
        view.recyclerViewChannel.layoutManager = LinearLayoutManager(requireContext())
        view.recyclerViewChannel.adapter = adapter
    }

    override fun getClient() = ChatClient.getInstance()
}