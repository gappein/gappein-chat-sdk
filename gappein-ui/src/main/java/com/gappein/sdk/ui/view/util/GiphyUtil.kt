package com.gappein.sdk.ui.view.util

import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType

open class GiphyUtil {
    companion object {
        private val mediaTypeConfig = arrayOf(
            GPHContentType.gif,
            GPHContentType.sticker,
            GPHContentType.text,
            GPHContentType.recents
        )

        val settings = GPHSettings(
            gridType = GridType.waterfall,
            useBlurredBackground = false,
            theme = GPHTheme.Light,
            stickerColumnCount = 3,
            mediaTypeConfig = mediaTypeConfig
        )
    }
}