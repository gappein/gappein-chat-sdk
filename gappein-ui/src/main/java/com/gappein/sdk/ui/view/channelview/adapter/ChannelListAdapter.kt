package com.gappein.sdk.ui.view.channelview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.channelview.viewholder.ChannelListViewHolder
import com.gappein.sdk.ui.view.channelview.viewholder.SearchViewHolder

class ChannelListAdapter(
    private val channel: ArrayList<Channel> = arrayListOf(),
    private val onUserClick: (User) -> Unit,
    private val onChannelClick: (Channel, User) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_CHANNEL = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))
        } else {
            ChannelListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false))
        }
    }

    fun addAll(channel: List<Channel>) {
        this.channel.clear()
        this.channel.addAll(channel)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            VIEW_TYPE_USER ->(holder as SearchViewHolder).bind(){}
            VIEW_TYPE_CHANNEL ->(holder as ChannelListViewHolder).bind(channel[position.minus(1)], onUserClick, onChannelClick)
        }
    }

    override fun getItemCount() = channel.count().plus(1)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_CHANNEL
        }
    }
}