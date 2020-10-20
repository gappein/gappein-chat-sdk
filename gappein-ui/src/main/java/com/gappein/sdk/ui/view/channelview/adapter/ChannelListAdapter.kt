package com.gappein.sdk.ui.view.channelview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.channelview.viewholder.ChannelListViewHolder
import java.util.*

class ChannelListAdapter(
    private val channel: ArrayList<Channel> = arrayListOf(),
    private val onUserClick: (User) -> Unit,
    private val onChannelClick: (Channel, User) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChannelListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        )

    }

    fun addAll(channel: List<Channel>) {
        this.channel.clear()
        this.channel.addAll(channel)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChannelListViewHolder).bind(
            channel[position],
            onUserClick,
            onChannelClick
        )
    }

    override fun getItemCount() = channel.count()

}