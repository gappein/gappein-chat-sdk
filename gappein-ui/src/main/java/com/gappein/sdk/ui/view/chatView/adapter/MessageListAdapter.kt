package com.gappein.sdk.ui.view.chatView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.binding.chatbinding.viewholder.ReceiverMessageListViewHolder
import com.gappein.sdk.ui.databinding.ItemImageReceivedMessageBinding
import com.gappein.sdk.ui.databinding.ItemImageSentMessageBinding
import com.gappein.sdk.ui.databinding.ItemReceivedMessageBinding
import com.gappein.sdk.ui.databinding.ItemSentMessageBinding
import com.gappein.sdk.ui.view.chatView.viewholder.ReceiveImageViewHolder
import com.gappein.sdk.ui.view.chatView.viewholder.SenderImageViewHolder
import com.gappein.sdk.ui.view.chatView.viewholder.SenderMessageListViewHolder
import java.util.*

class MessageListAdapter(
    private val messages: ArrayList<Message> = arrayListOf(),
    private val chatClient: ChatClient,
    private val onImageClick: (String) -> Unit,
    private val onMessageClick: (Message) -> Unit,
    private val onMessageLike: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filteredList = mutableListOf<Message>()

    companion object {
        private const val VIEW_TYPE_SENDER_TEXT = 1
        private const val VIEW_TYPE_SENDER_IMAGE = 2
        private const val VIEW_TYPE_RECEIVER_TEXT = 3
        private const val VIEW_TYPE_RECEIVER_IMAGE = 4
        private const val GIPHY = "giphy"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER_TEXT -> {
                val binding = ItemSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SenderMessageListViewHolder(binding)
            }
            VIEW_TYPE_SENDER_IMAGE -> {
                val binding = ItemImageSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SenderImageViewHolder(binding)
            }
            VIEW_TYPE_RECEIVER_IMAGE -> {
                val binding = ItemImageReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ReceiveImageViewHolder(binding)
            }
            else -> {
                val binding = ItemReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ReceiverMessageListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            VIEW_TYPE_SENDER_TEXT -> (holder as SenderMessageListViewHolder).bind(
                position,
                filteredList.toList(),
                onMessageClick,
                onMessageLike
            )

            VIEW_TYPE_RECEIVER_TEXT -> (holder as ReceiverMessageListViewHolder).bind(
                filteredList.toList(),
                position,
                onMessageLike
            )

            VIEW_TYPE_RECEIVER_IMAGE -> (holder as ReceiveImageViewHolder).bind(
                filteredList[position],
                onImageClick,
                onMessageLike
            )

            VIEW_TYPE_SENDER_IMAGE -> (holder as SenderImageViewHolder).bind(
                filteredList[position],
                onImageClick,
                onMessageLike
            )
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
        return if (filteredList[position].isUrl || filteredList[position].message.contains(GIPHY)) {
            if (chatClient.getUser().token == filteredList[position].receiver.token) {
                VIEW_TYPE_RECEIVER_IMAGE
            } else {
                VIEW_TYPE_SENDER_IMAGE
            }
        } else {
            if (chatClient.getUser().token == filteredList[position].receiver.token) {
                VIEW_TYPE_RECEIVER_TEXT
            } else {
                VIEW_TYPE_SENDER_TEXT
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