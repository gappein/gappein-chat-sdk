package com.gappein.coroutine.sdk.data.storage

import android.net.Uri
import com.gappein.coroutine.sdk.model.UploadResponse
import com.gappein.coroutine.sdk.model.User
import java.io.File

interface FirebaseStorageManager {

    suspend fun uploadUserImage(user: User, file: File): User

    suspend fun uploadMessageImage(file: Uri, receiver: User, sender: User): UploadResponse

    suspend fun uploadChatBackgroundImage(
        file: Uri,
        channelId: String
    ): UploadResponse

    suspend fun uploadBackupChat(
        file: File,
        channelId: String,
    ): UploadResponse

}