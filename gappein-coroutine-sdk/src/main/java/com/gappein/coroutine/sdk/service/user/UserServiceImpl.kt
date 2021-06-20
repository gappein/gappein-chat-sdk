package com.gappein.coroutine.sdk.service.user

import com.gappein.coroutine.sdk.client.ChatClient
import com.gappein.coroutine.sdk.model.User
import com.gappein.coroutine.sdk.util.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl : UserService {

    companion object {

        private val currentUser by lazy { ChatClient.getInstance().getUser() }

        const val TRUE = "true"

        const val IS_ONLINE = "online"

        private const val STATUS = "textStatus"

        const val LAST_ONLINE_AT = "lastOnlineAt"

        private const val USER_COLLECTION = "users"

        private val database: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

        private val userDatabaseReference by lazy { database.collection(USER_COLLECTION) }

    }

    override suspend fun createUser(user: User): BaseResponse<User> {
        val reference = userDatabaseReference.document(user.token)
        val result = reference.getValue()
        return if (result.exists()) {
            BaseResponse(exception = Exception(""))
        } else {
            val newUser = reference.setValue(user)
            BaseResponse(data = newUser)
        }
    }

    override suspend fun getUserByToken(token: String): User {
        return suspendCoroutine { continuation ->
            userDatabaseReference.get()
                .addOnSuccessListener { result ->
                    val user = result.toObject<User>().find {
                        it.getUserWithToken(token)
                    }
                    user?.let { continuation.resume(it) }
                }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

    override suspend fun isUserOnline(token: String): Pair<Boolean, String> {
        return suspendCoroutine { continuation ->
            val userChannelReference = userDatabaseReference.document(token)
            userChannelReference.getRealtimeValue { value, message ->
                val data = value?.data as Map<String, User>
                if (data[UserServiceImpl.IS_ONLINE].toString() == UserServiceImpl.TRUE) {
                    continuation.resume(Pair(true, ""))
                } else {
                    continuation.resume(
                        Pair(
                            false,
                            data[UserServiceImpl.LAST_ONLINE_AT].toString()
                        )
                    )
                }
            }
        }
    }

    override suspend fun setUserOnline(status: Boolean): Boolean {
        return suspendCoroutine { continuation ->
            val userChannelReference = userDatabaseReference.document(currentUser.token)
            val request = Pair(IS_ONLINE, status)
            userChannelReference.updateDocument(request) { isSuccessful, exception ->
                if (isSuccessful) {
                    continuation.resume(true)
                } else {
                    if (exception != null) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
        }
    }

    override suspend fun setUserStatus(status: String): Boolean {
        return suspendCoroutine { continuation ->
            val userChannelReference = userDatabaseReference.document(currentUser.token)
            val request = Pair(IS_ONLINE, status)
            userChannelReference.updateDocument(request) { isSuccessful, exception ->
                if (isSuccessful) {
                    continuation.resume(true)
                } else {
                    if (exception != null) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
        }
    }

    override suspend fun getUserStatus(token: String): String {
        return suspendCoroutine { continuation ->
            userDatabaseReference.document(token)
                .getRealtimeValue { value, exception ->
                    if (exception != null) {
                        continuation.resumeWithException(exception)
                    } else {
                        val status = value?.data?.get(STATUS) as String
                        continuation.resume(status)
                    }
                }
        }
    }
}