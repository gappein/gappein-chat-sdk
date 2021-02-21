package com.gappein.sdk.service.user

import com.gappein.sdk.model.User

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
interface UserService {

    fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit)

    fun getUserByToken(token: String, onSuccess: (user: User) -> Unit, onError: (Exception) -> Unit)

    fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit)

    fun setUserOnline(
        status: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun setUserStatus(status: String, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getUserStatus(token: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit)

}