package com.gappein.coroutine.sdk.data.storage

import android.net.Uri
import com.gappein.coroutine.sdk.model.User
import java.io.File

interface FirebaseStorageManager {

    fun uploadUserImage(user: User, file: File,onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun uploadMessageImage(file: Uri, receiver: User, sender: User, onSuccess: (String) -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit)

    fun uploadChatBackgroundImage(file: Uri, channelId:String, onSuccess: (String) -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit)

    fun uploadBackupChat(file: File, channelId:String, onSuccess: (String) -> Unit, onProgress: (Int) -> Unit, onError: (Exception) -> Unit)

}