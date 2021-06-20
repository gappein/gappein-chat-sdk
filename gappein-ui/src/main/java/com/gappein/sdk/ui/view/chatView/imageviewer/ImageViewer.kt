package com.gappein.sdk.ui.view.chatView.imageviewer

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.ItemPhotoViewerBinding

fun openImage(context: Context, url: String) {
    val binding = ItemPhotoViewerBinding.inflate(LayoutInflater.from(context), null, false)
    val popupWindow =
        PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    popupWindow.animationStyle = R.style.popup_window_animation
    Glide.with(context).load(url).into(binding.imageViewPreview)
    binding.textViewButton.setOnClickListener { popupWindow.dismiss() }
    popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

}