package com.gappein.sdk.ui.view.util

import android.view.MenuItem
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