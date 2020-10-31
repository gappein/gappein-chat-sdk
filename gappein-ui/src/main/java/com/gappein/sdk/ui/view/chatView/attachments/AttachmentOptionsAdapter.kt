package com.gappein.sdk.ui.view.chatView.attachments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.chatView.attachments.AttachmentOptionsAdapter.*
import com.gappein.sdk.ui.view.util.CameraOption
import com.gappein.sdk.ui.view.util.GalleryOption
import kotlinx.android.synthetic.main.item_attachment.view.*

/**
 * Adapter class to handle list of Attachments
 */

class AttachmentOptionsAdapter(
    private val attachmentOptions: ArrayList<String>,
    private val onOptionClick: (String) -> Unit
) : RecyclerView.Adapter<AttachmentOptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentOptionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_attachment, parent, false)
        return AttachmentOptionViewHolder(view, onOptionClick)
    }

    override fun getItemCount() = attachmentOptions.size

    override fun onBindViewHolder(holder: AttachmentOptionViewHolder, position: Int) {
        holder.bind(attachmentOptions[position])
    }

    class AttachmentOptionViewHolder(itemView: View, private val onOptionClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(attachment: String) {
            itemView.apply {
                tv_attachment_option_name.text = attachment
                when (attachment) {
                    CameraOption().optionName -> {
                        iv_attachment_option.setImageDrawable(
                            context.getDrawable(
                                R.drawable.ic_camera
                            )
                        )
                    }
                    GalleryOption().optionName -> {
                        iv_attachment_option.setImageDrawable(
                            context.getDrawable(
                                R.drawable.ic_gallery
                            )
                        )
                    }
                }
                setOnClickListener {
                    onOptionClick(attachment)
                }
            }
        }
    }

}