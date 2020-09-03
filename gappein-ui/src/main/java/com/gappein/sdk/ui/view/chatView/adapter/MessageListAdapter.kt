package com.gappein.sdk.ui.view.chatView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.chatView.viewholder.ReceiveImageViewHolder
import com.gappein.sdk.ui.view.chatView.viewholder.ReceiverMessageListViewHolder
import com.gappein.sdk.ui.view.chatView.viewholder.SenderImageViewHolder
import com.gappein.sdk.ui.view.chatView.viewholder.SenderMessageListViewHolder
import java.util.*
import kotlin.collections.ArrayList

class MessageListAdapter(
    private var messages: ArrayList<Message> = ArrayList<Message>(),
    private val chatClient: ChatClient,
    private val onImageClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filteredList = mutableListOf<Message>()

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_SENDER_IMAGE = 2
        private const val VIEW_TYPE_RECEIVER = 3
        private const val VIEW_TYPE_RECEIVER_IMAGE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENDER) {
            SenderMessageListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sent_message, parent, false)
            )
        } else if (viewType == VIEW_TYPE_SENDER_IMAGE) {
            SenderImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_sent_message, parent, false)
            )
        } else if (viewType == VIEW_TYPE_RECEIVER_IMAGE) {
            ReceiveImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_received_message, parent, false)
            )
        } else {
            ReceiverMessageListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_received_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENDER) {
            (holder as SenderMessageListViewHolder).bind(position, filteredList.toList())
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVER) {
            (holder as ReceiverMessageListViewHolder).bind(filteredList.toList(), position)
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVER_IMAGE) {
            (holder as ReceiveImageViewHolder).bind(filteredList[position], position,onImageClick)
        } else if (getItemViewType(position) == VIEW_TYPE_SENDER_IMAGE) {
            (holder as SenderImageViewHolder).bind(filteredList[position], position,onImageClick)
        }
    }

    override fun getItemCount() = filteredList.count()

    fun addAll(it: List<Message>) {
        filteredList.run {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (filteredList[position].isUrl) {
            if (chatClient.getUser().token == filteredList[position].receiver.token) {
                VIEW_TYPE_RECEIVER_IMAGE
            } else {
                VIEW_TYPE_SENDER_IMAGE
            }
        } else {
            if (chatClient.getUser().token == filteredList[position].receiver.token) {
                VIEW_TYPE_RECEIVER
            } else {
                VIEW_TYPE_SENDER
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList = if (charSearch.isEmpty()) {
                    messages
                } else {
                    val resultList = mutableListOf<Message>()
                    for (row in messages) {
                        if (row.message.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as List<Message>).toMutableList()
                notifyDataSetChanged()
            }
        }
    }
}