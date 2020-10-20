package com.gappein.sdk.ui.view.channelview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.view.channelview.`interface`.OnChannelClick
import com.gappein.sdk.ui.view.channelview.adapter.ChannelListAdapter
import com.gappein.sdk.ui.view.chatView.MessageListActivity
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.fragment_channel_list.view.*


class ChannelListFragment : Fragment(), ChatBaseView {

    companion object {

        /**
         * Returns instance of ChanneListFragment
         *
         */
        @JvmStatic
        fun newInstance() = ChannelListFragment()

        const val TAG = "ChannelListFragment"
    }

    private lateinit var adapter: ChannelListAdapter
    private lateinit var onUserClick: OnChannelClick

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChannelList(view)
        fetchChannels(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onUserClick = (context) as OnChannelClick
        } catch (ex: Exception) {
            throw NotImplementedError("Implement OnChannelClick on your base activity")
        }
    }

    private fun fetchChannels(view: View) {
        getClient().getUserChannels {
            if (it.isNotEmpty()) {
                adapter.addAll(it)
                view.linearLayoutNoChatFound.hide()
                view.recyclerViewChannel.show()
            } else {
                view.linearLayoutNoChatFound.show()
                view.recyclerViewChannel.hide()
            }
        }
    }

    private fun setupChannelList(view: View) {
        adapter = ChannelListAdapter(onUserClick = {
            onUserClick.onUserClick(it)
        }, onChannelClick = { channel, user ->
            startActivity(MessageListActivity.buildIntent(requireContext(), channel.id, user))
        })
        view.recyclerViewChannel.layoutManager = LinearLayoutManager(requireContext())
        view.recyclerViewChannel.adapter = adapter
    }

    override fun getClient() = ChatClient.getInstance()

}