package com.gappein.sdk.data.storage

import android.net.Uri
import com.gappein.sdk.data.uploadUserImage
import com.gappein.sdk.model.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FirebaseStorageManagerImpl : FirebaseStorageManager {

    companion object {
        private const val IMAGES = "images"
    }

    private val manager = FirebaseStorage.getInstance()
    private val storageReference = manager.reference

    override fun uploadUserImage(user: User, file: File, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {
        manager.uploadUserImage(
            user,
            file,
            { onSuccess(User(token = user.token,createdAt =  user.createdAt, profileImageUrl = it,name =  user.name)) },
            { onError(it) })
    }

    override fun uploadMessageImage(file: Uri, receiver: User, sender: User, onSuccess: (String) -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit) {
        val userList = listOf(sender.token, receiver.token)
        val messagePath = "${userList.sorted()}${System.currentTimeMillis()}"
        val imagePath = "$IMAGES/$messagePath"
        val reference = storageReference.child(imagePath)
        reference.putFile(file)
            .addOnSuccessListener { reference.downloadUrl.addOnSuccessListener { onSuccess(it.toString()) } }
            .addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                onProgress(progress.toInt())
            }
            .addOnFailureListener { onError(it) }
    }
}