package com.gappein.sdk.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.core.text.color


fun String.checkForAnnotation(): String {
    if (this.contains("@")) {
        Log.d("dsfdsdf", this)
        val text = SpannableStringBuilder()
            .color(Color.BLUE) { append(this) }
        return text.toString()
    }
    return this
}

fun TextView.colorize(subStringToColorize: String) {

    if (subStringToColorize.contains("@")) {
        val spannable: Spannable = SpannableString(text)

        val startIndex = text.indexOf(subStringToColorize, startIndex = 0, ignoreCase = false)
        val endIndex = startIndex + subStringToColorize.length


        if (startIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.BLUE),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    } else {
        text = subStringToColorize
    }
}