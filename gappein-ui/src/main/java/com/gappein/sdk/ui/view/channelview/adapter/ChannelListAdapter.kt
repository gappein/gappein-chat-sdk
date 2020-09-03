package com.gappein.sdk.ui.view.channelview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Channel
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.channelview.viewholder.ChannelListViewHolder

class ChannelListAdapter(private val channel: ArrayList<Channel> = arrayListOf()) :
    RecyclerView.Adapter<ChannelListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelListViewHolder {
        return ChannelListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_channel, parent, false)
        )
    }

    fun addAll(channel: List<Channel>) {
        this.channel.clear()
        this.channel.addAll(channel)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ChannelListViewHolder, position: Int) {
        holder.bind(channel[position])
    }

    override fun getItemCount() = channel.count()

}