package com.gappein.sdk.ui.view.channelview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.databinding.FragmentChannelListBinding
import com.gappein.sdk.ui.view.channelview.`interface`.OnChannelClick
import com.gappein.sdk.ui.view.channelview.adapter.ChannelListAdapter
import com.gappein.sdk.ui.view.chatView.MessageListActivity
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show


class ChannelListFragment : Fragment(), ChatBaseView {

    private var _binding: FragmentChannelListBinding? = null
    private val binding: FragmentChannelListBinding
        get() = _binding!!

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
        _binding = FragmentChannelListBinding.inflate(inflater, container, false)
        return binding.root
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
                binding.linearLayoutNoChatFound.hide()
                binding.recyclerViewChannel.show()
            } else {
                binding.linearLayoutNoChatFound.show()
                binding.recyclerViewChannel.hide()
            }
        }
    }

    private fun setupChannelList(view: View) {
        adapter = ChannelListAdapter(requireContext(), onUserClick = {
            onUserClick.onUserClick(it)
        }, onChannelClick = { channel, user ->
            startActivity(MessageListActivity.buildIntent(requireContext(), channel.id, user))
        })
        binding.recyclerViewChannel.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChannel.adapter = adapter
    }

    override fun getClient(): ChatClient = ChatClient.getInstance()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}