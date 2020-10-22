package com.gappein.sdk.ui.view.util

import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.gappein.sdk.ui.view.chatView.MessageListActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun EditText.addTextChangeListener(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.addTypeChangeListener(onSuccess: (Boolean) -> Unit) {
    val currentTypingInterval = 800
    var userCurrentTypingState = false
    val handler = Handler(Looper.getMainLooper())
    val stoppedTypingNotifier = Runnable { //part A of the magic...
        onSuccess(false)
        userCurrentTypingState = false
    }

    this.addTextChangeListener {
        if (!userCurrentTypingState) {
            onSuccess(true)
            userCurrentTypingState = true
        }
        handler.removeCallbacks(stoppedTypingNotifier)
        handler.postDelayed(stoppedTypingNotifier, currentTypingInterval.toLong())
    }
}

@Throws(IOException::class)
fun MessageListActivity.createImageFile(): File? {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    val mFileName = "JPEG_" + timeStamp + "_"
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(mFileName, ".jpg", storageDir)
}

/**
 * Get real file path from URI
 */
fun MessageListActivity.getRealPathFromUri(contentUri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = contentUri?.let { contentResolver.query(it, proj, null, null, null) }
        assert(cursor != null)
        val column_index: Int = (cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            ?: cursor?.moveToFirst()) as Int
        if (cursor?.moveToFirst() == true) cursor.getString(column_index) else null
    } finally {
        cursor?.close()
    }
}
