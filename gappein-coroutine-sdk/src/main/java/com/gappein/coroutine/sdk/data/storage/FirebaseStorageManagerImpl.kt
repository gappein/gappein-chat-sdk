package com.gappein.coroutine.sdk.data.storage

import android.net.Uri
import androidx.core.net.toUri
import com.gappein.coroutine.sdk.data.uploadUserImage
import com.gappein.coroutine.sdk.model.UploadResponse
import com.gappein.coroutine.sdk.model.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageManagerImpl : FirebaseStorageManager {

    companion object {
        private const val IMAGES = "images"
    }

    private val manager = FirebaseStorage.getInstance()
    private val storageReference = manager.reference

    override suspend fun uploadUserImage(user: User, file: File): User {
        return suspendCoroutine { continuation ->
            manager.uploadUserImage(
                user,
                file, {
                    continuation.resume(
                        User(
                            token = user.token,
                            createdAt = user.createdAt,
                            profileImageUrl = it,
                            name = user.name
                        )
                    )
                }, { continuation.resumeWithException(it) })
        }
    }

    override suspend fun uploadMessageImage(
        file: Uri,
        receiver: User,
        sender: User
    ): UploadResponse {
        val userList = listOf(sender.token, receiver.token)
        val messagePath = "${userList.sorted()}${System.currentTimeMillis()}"
        val imagePath = "$IMAGES/$messagePath"
        val reference = storageReference.child(imagePath)

        return suspendCoroutine { continuation ->
            reference.putFile(file)
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        continuation.resume(UploadResponse(it.toString()))
                    }
                }
                .addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    continuation.resume(UploadResponse(progress = progress.toInt()))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun uploadChatBackgroundImage(
        file: Uri,
        channelId: String
    ): UploadResponse {

        val messagePath = "${channelId}/${System.currentTimeMillis()}"
        val reference = storageReference.child(messagePath)
        return suspendCoroutine { continuation ->
            reference.putFile(file)
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        continuation.resume(UploadResponse(it.toString()))
                    }
                }
                .addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    continuation.resume(UploadResponse(progress = progress.toInt()))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun uploadBackupChat(file: File, channelId: String): UploadResponse {
        val messagePath = "${channelId}/${System.currentTimeMillis()}.json"
        val reference = storageReference.child(messagePath)
        return suspendCoroutine { continuation ->
            reference.putFile(file.toUri())
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        continuation.resume(UploadResponse(it.toString()))
                    }
                }
                .addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    continuation.resume(UploadResponse(progress = progress.toInt()))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }
}