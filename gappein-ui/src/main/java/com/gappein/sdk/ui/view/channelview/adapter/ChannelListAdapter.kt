package com.gappein.sdk.ui.view.channelview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Channel
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.channelview.viewholder.ChannelListViewHolder
import com.gappein.sdk.ui.view.channelview.viewholder.SearchViewHolder
import java.util.*

class ChannelListAdapter(
    private val channel: ArrayList<Channel> = arrayListOf(),
    private val onUserClick: (User) -> Unit,
    private val onChannelClick: (Channel, User) -> Unit,
    private val onTextChange: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var channelList = mutableListOf<Channel>()

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_CHANNEL = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            SearchViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
            )
        } else {
            ChannelListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
            )
        }
    }

    fun addAll(channel: List<Channel>) {
        this.channel.clear()
        channelList.clear()
        channelList.addAll(channel)
        this.channel.addAll(channel)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_USER -> (holder as SearchViewHolder).bind(onTextChange)
            VIEW_TYPE_CHANNEL -> (holder as ChannelListViewHolder).bind(
                channelList[position.minus(1)],
                onUserClick,
                onChannelClick
            )
        }
    }

    override fun getItemCount() = channelList.count().plus(1)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_CHANNEL
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                channelList = if (charSearch.isEmpty()) {
                    channel
                } else {
                    val resultList = mutableListOf<Channel>()
                    for (row in channel) {
                        Log.d(
                            "fsdfsd",
                            row.users.contains(User(name = charSearch.toLowerCase(Locale.ROOT)))
                                .toString()
                        )
                        if (row.users.contains(User(name = charSearch.toLowerCase(Locale.ROOT)))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = channelList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                channelList = (results?.values as List<Channel>).toMutableList()
                notifyDataSetChanged()
            }
        }
    }


}