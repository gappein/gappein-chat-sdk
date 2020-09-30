package com.gappein.sdk.ui.view.channelview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.ui.view.util.addTextChangeListener
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(onTextChange: (String) -> Unit) {
        view.editTextSearchUser.addTextChangeListener {
            onTextChange(it)
        }
    }
}