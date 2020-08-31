package com.gappein.sdk.ui.chatView.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.model.Message
import kotlinx.android.synthetic.main.item_sent_message.view.*
import java.text.SimpleDateFormat
import java.util.*

class SenderMessageListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(message: Message, position: Int) {
        view.sentTextMessage.text = message.message

        message.timeStamp?.let { epochToIso8601(it).toString() }?.let { Log.d("mnbhjb", it) }

    }

    fun epochToIso8601(time: Long): String? {
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(time)
        return resultdate.toString()
    }
}