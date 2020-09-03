package com.gappein.sdk.ui.view.chatView.imageviewer

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.gappein.sdk.ui.R
import kotlinx.android.synthetic.main.item_photo_viewer.view.*

fun openImage(context: Context, url: String) {

    val view = LayoutInflater.from(context).inflate(R.layout.item_photo_viewer, null, false)
    val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    Glide.with(context).load(url).into(view.imageViewPreview)

    view.textViewButton.setOnClickListener { popupWindow.dismiss() }

    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

}