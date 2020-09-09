package com.gappein.sdk.ui.view.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


object DatesUtil {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    private fun currentDate(): Date {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.getTime()
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeAgo(time: Long?): String? {
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = time?.let { Date(it) }
        return resultdate?.let { getTimeAgo(it) }
    }


    private fun getTimeAgo(date: Date): String? {
        var time: Long = date.getTime()
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now: Long = currentDate().getTime()
        if (time > now || time <= 0) {
            return "in the future"
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "moments ago"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a min ago"
        } else if (diff < 60 * MINUTE_MILLIS) {
            diff.div(MINUTE_MILLIS).toString() + " mins ago"
        } else if (diff < 2 * HOUR_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            diff.div(HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            diff.div(DAY_MILLIS).toString() + " days ago"
        }
    }


}