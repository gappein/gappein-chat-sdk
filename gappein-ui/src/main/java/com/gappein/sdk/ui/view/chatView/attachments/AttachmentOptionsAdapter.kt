package com.gappein.sdk.ui.view.chatView.attachments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.ItemAttachmentBinding
import com.gappein.sdk.ui.databinding.ItemChannelBinding
import com.gappein.sdk.ui.view.chatView.attachments.AttachmentOptionsAdapter.*
import com.gappein.sdk.ui.view.util.CameraOption
import com.gappein.sdk.ui.view.util.GalleryOption

/**
 * Adapter class to handle list of Attachments
 */

class AttachmentOptionsAdapter(
    private val attachmentOptions: ArrayList<String>,
    private val onOptionClick: (String) -> Unit
) : RecyclerView.Adapter<AttachmentOptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentOptionViewHolder {
        val binding =
            ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AttachmentOptionViewHolder(binding, onOptionClick)
    }

    override fun getItemCount() = attachmentOptions.size

    override fun onBindViewHolder(holder: AttachmentOptionViewHolder, position: Int) {
        holder.bind(attachmentOptions[position])
    }

    class AttachmentOptionViewHolder(
        private val binding: ItemAttachmentBinding,
        private val onOptionClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(attachment: String) {
            binding.apply {
                tvAttachmentOptionName.text = attachment
                when (attachment) {
                    CameraOption().optionName -> {
                        ivAttachmentOption.setImageDrawable(
                            root.context.getDrawable(
                                R.drawable.ic_camera
                            )
                        )
                    }
                    GalleryOption().optionName -> {
                        ivAttachmentOption.setImageDrawable(
                            root.context.getDrawable(
                                R.drawable.ic_gallery
                            )
                        )
                    }
                }
                root.setOnClickListener {
                    onOptionClick(attachment)
                }
            }
        }
    }

}