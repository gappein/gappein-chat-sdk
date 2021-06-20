package com.gappein.coroutine.sdk.util

import android.content.Context
import com.google.gson.Gson
import java.io.File

fun <T> Gson.getFile(context: Context, channelId: String, list: List<T>): File {
    val file = File(context.filesDir, "${channelId}.json")
    file.createNewFile()
    file.appendText(toJson(list))
    return file
}