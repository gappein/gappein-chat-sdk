package com.gappein.sdk.service.user

import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.User
import com.gappein.sdk.util.*
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
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

    override fun createUser(user: User, onSuccess: (User) -> Unit, onError: (Exception) -> Unit) {

        val reference = userDatabaseReference.document(user.token)

        reference.getValue({ _user ->
            if (!_user.exists()) {
                reference.setValue(user, {
                    onSuccess(it)
                }, {
                    onError(it)
                })
            }
        }, {
            onError(it)
        })

    }

    override fun getUserByToken(
        token: String,
        onSuccess: (user: User) -> Unit,
        onError: (Exception) -> Unit
    ) {

        userDatabaseReference.get()
            .addOnSuccessListener { result ->
                val user = result.getObject<User>().find {
                    it.getUser(token)
                }
                user?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception -> onError(exception) }

    }

    override fun isUserOnline(token: String, onSuccess: (Boolean, String) -> Unit) {
        val userChannelReference = userDatabaseReference.document(token)
        userChannelReference.getRealtimeValue { value, message ->
            val data = value?.data as Map<String, User>
            if (data[IS_ONLINE].toString() == TRUE) {
                onSuccess(true, "")
            } else {
                onSuccess(false, data[LAST_ONLINE_AT].toString())
            }
        }

    }


    override fun setUserOnline(
        status: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userChannelReference = userDatabaseReference.document(currentUser.token)
        userChannelReference.updateDocument(Pair(IS_ONLINE, status)) { isSuccessful, exception ->
            if (isSuccessful) {
                onSuccess()
            } else {
                exception?.let { onError(it) }
            }
        }
    }

    override fun setUserStatus(
        status: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        userDatabaseReference.document(currentUser.token)
            .updateDocument(Pair(STATUS, status)) { isSuccessful, exception ->
                if (isSuccessful) {
                    onSuccess()
                } else {
                    exception?.let { onError(it) }
                }
            }
    }

    override fun getUserStatus(
        token: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        userDatabaseReference.document(token)
            .getRealtimeValue { value, error ->
                if (error != null) {
                    onError(error)
                } else {
                    val status = value?.data?.get(STATUS) as String
                    onSuccess(status)
                }
            }
    }
    
}