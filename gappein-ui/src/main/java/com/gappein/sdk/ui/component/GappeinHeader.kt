package com.gappein.sdk.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import kotlinx.android.synthetic.main.header_view.view.*

class GappeinHeader : LinearLayout, ChatBaseView {

    private lateinit var view: View

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews()
    }

    private fun initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.header_view, this, true)
    }

    fun init(channelId: String) {
        getClient().getChannelRecipientUser(channelId) {
            view.titleToolbar.text = it.name
            Glide.with(view).load(it.profileImageUrl).into(view.avatarImageView)
        }
    }

    fun setOnBackPressed(onBackPress: () -> Unit) {
        view.imageViewBack.setOnClickListener { onBackPress.invoke() }
    }

    override fun getClient() = ChatClient.getInstance()

}