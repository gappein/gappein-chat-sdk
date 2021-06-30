package com.gappein.sdk.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.databinding.HeaderViewBinding
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show

class GappeinHeader : LinearLayout, ChatBaseView {

    private var binding =
        HeaderViewBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var toolbar: Toolbar

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    init { // inflate binding and add as view
    }


    fun init(channelId: String) {
        getClient().getChannelRecipientUser(channelId) {
            getClient().getTypingStatus(channelId, it.token) { typingStatus ->
                if (typingStatus != "-") {
                    binding.typingStatus.show()
                    binding.typingStatus.text = typingStatus
                } else {
                    binding.typingStatus.hide()
                }
            }
            binding.titleToolbar.text = it.name

            Glide.with(binding.root.context)
                .load(it.profileImageUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.avatarImageView)
        }
    }

    fun init(channelId: () -> String) {
        init(channelId.invoke())
    }

    fun setMenu(@MenuRes menu: Int) {
        binding.toolbar.inflateMenu(menu)
    }

    fun setOnBackPressed(onBackPress: () -> Unit) {
        binding.imageViewBack.setOnClickListener { onBackPress.invoke() }
    }

    override fun getClient() = ChatClient.getInstance()

}