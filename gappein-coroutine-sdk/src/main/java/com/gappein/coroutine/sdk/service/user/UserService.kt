package com.gappein.coroutine.sdk.service.user

import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.util.BaseResponse

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
interface UserService {

    suspend fun createUser(user: User): BaseResponse<User>

    suspend fun getUserByToken(token: String): User

    suspend fun isUserOnline(token: String): Pair<Boolean, String>

    suspend fun setUserOnline(status: Boolean): Boolean

    suspend fun setUserStatus(status: String): Boolean

    suspend fun getUserStatus(token: String): String
}