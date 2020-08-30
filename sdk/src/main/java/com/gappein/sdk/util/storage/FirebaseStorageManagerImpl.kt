package com.gappein.sdk.util.storage

import android.net.Uri
import com.gappein.sdk.model.User
import com.gappein.sdk.util.uploadUserImage
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FirebaseStorageManagerImpl : FirebaseStorageManager {

    private val manager = FirebaseStorage.getInstance()

    companion object {
        private val IMAGES = "images"
    }

    override fun uploadUserImage(
        user: User,
        file: File,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        manager.uploadUserImage(
            user,
            file,
            { onSuccess(User(user.token, user.createdAt, it.toString(), user.name)) },
            { onError(it) })
    }

    override fun uploadMessageImage(
        file: Uri,
        receiver: String,
        sender: String,
        onSuccess: (String) -> Unit,
        onProgress: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val storageReference = manager.reference
        val userList = listOf(sender, receiver)
        val messagePath = userList.sorted().toString()
        val imagePath = "$IMAGES/$messagePath"
        val ref = storageReference.child(imagePath)
        ref.putFile(file)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    onSuccess(it.toString())
                }
            }
            .addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                onProgress(progress.toInt())
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}