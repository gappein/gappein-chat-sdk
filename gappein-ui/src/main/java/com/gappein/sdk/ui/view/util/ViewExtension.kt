package com.gappein.sdk.ui.view.util

import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.SearchView

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}


fun SearchView.addFilter(menuItem: MenuItem, onQueryChange: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            menuItem.collapseActionView();
            return false;
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { onQueryChange(it) }
            return false
        }

    })
}

fun View.onDoubleTapListener(onDoubleTapListener: ()->Unit) {
    val view = this
    view.setOnTouchListener(object : View.OnTouchListener {
        val gestureDetector =
            GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    onDoubleTapListener()
                    return super.onDoubleTap(e)
                }
            })

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(event)
            return true
        }
    })
}