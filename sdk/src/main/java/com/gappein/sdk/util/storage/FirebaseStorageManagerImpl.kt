package com.gappein.sdk.util.storage

import com.gappein.sdk.model.User
import com.gappein.sdk.util.uploadUserImage
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FirebaseStorageManagerImpl : FirebaseStorageManager {

    private val manager = FirebaseStorage.getInstance()

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
        file: File,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {

    }


}