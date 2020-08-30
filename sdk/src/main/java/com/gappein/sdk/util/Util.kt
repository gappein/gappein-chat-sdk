package com.gappein.sdk.util

import androidx.core.net.toUri
import com.gappein.sdk.model.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File

fun FirebaseStorage.uploadUserImage(
    user: User,
    file: File,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val imagePath = "images" + "/" + user.token
    val storage = this.getReference(imagePath)
    storage.putFile(file.toUri())
        .addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                if (it != null) {
                    onSuccess(it.toString())
                }
            }
        }
        .addOnFailureListener {
            onError(it)
        }
}