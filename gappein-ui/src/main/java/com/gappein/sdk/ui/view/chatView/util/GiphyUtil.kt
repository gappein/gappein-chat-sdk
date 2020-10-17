package com.gappein.sdk.ui.view.chatView.util

import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyDialogFragment

private val mediaTypeConfig = arrayOf(
    GPHContentType.gif,
    GPHContentType.sticker,
    GPHContentType.text,
    GPHContentType.recents
)

val giphySettings = GPHSettings(
    gridType = GridType.waterfall,
    useBlurredBackground = false,
    theme = GPHTheme.Light,
    stickerColumnCount = 3,
    mediaTypeConfig = mediaTypeConfig
)

fun gifSelectionListener(onSelected: (String) -> Unit) =
    object : GiphyDialogFragment.GifSelectionListener {
        override fun onGifSelected(
            media: Media,
            searchTerm: String?,
            selectedContentType: GPHContentType
        ) {
            onSelected(media.id)
        }

        override fun onDismissed(selectedContentType: GPHContentType) {}

        override fun didSearchTerm(term: String) {}
    }
