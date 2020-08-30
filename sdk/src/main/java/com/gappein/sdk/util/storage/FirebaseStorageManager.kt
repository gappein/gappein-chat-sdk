package com.gappein.sdk.util.storage

import com.gappein.sdk.model.User
import java.io.File

interface FirebaseStorageManager {

    fun uploadUserImage(user: User, file: File,onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun uploadMessageImage(file: File,onSuccess: (String) -> Unit, onError: (Exception) -> Unit)

}