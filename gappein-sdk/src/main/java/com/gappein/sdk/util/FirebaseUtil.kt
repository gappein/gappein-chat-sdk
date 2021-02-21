package com.gappein.sdk.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import timber.log.Timber

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
inline fun <reified T> DocumentReference.getDocumentListByName(
    directory: String,
    crossinline returnResult: (result: List<T>) -> Unit
) {

    this.collection(directory)
        .get()
        .addOnSuccessListener { documentSnapshot ->
            returnResult(documentSnapshot.getObject())
        }

        .addOnFailureListener { exception ->
            Timber.tag("Gappein Exception:").w(exception, "Error getting documents: ")
        }
}

inline fun DocumentReference.updateDocument(
    updateValue: Pair<String, Any>,
    crossinline returnResult: (Boolean, Exception?) -> Unit
) {

    this.update(updateValue.first, updateValue.second)
        .addOnSuccessListener {
            returnResult(true, null)
        }
        .addOnFailureListener { exception ->
            returnResult(false, exception)
            Timber.tag("Gappein Exception:").w(exception, "Error update documents: ")
        }
}

inline fun DocumentReference.getValue(
    crossinline returnResult: (DocumentSnapshot) -> Unit,
    crossinline returnError: (Exception) -> Unit
) {
    this.get()
        .addOnSuccessListener {
            returnResult(it)
        }
        .addOnFailureListener { exception ->
            returnError(exception)
            Timber.tag("Gappein Exception:").w(exception, "Error getting documents: ")
        }
}

inline fun <reified T> DocumentReference.setValue(
    data: T,
    crossinline returnResult: (T) -> Unit,
    crossinline returnError: (Exception) -> Unit
) {
    this.set(data as Any)
        .addOnSuccessListener {
            returnResult(data)
        }
        .addOnFailureListener { exception ->
            returnError(exception)
            Timber.tag("Gappein Exception:").w(exception, "Error setting documents: ")
        }
}

inline fun <reified T> DocumentReference.addNewDocument(
    data: T,
    crossinline returnResult: (T) -> Unit,
    crossinline returnError: (Exception) -> Unit
) {
    this.set(data as Any)
        .addOnSuccessListener {
            returnResult(data)
        }
        .addOnFailureListener { exception ->
            returnError(exception)
            Timber.tag("Gappein Exception:").w(exception, "Error setting documents: ")
        }
}

inline fun <reified T> CollectionReference.getAllDocument(
    data: T,
    crossinline returnResult: (List<T>) -> Unit,
    crossinline returnError: (Exception) -> Unit
) {
    this.get()
        .addOnSuccessListener { documentSnapshot ->
            returnResult(documentSnapshot.getObject())
        }
        .addOnFailureListener { exception ->
            returnError(exception)
            Timber.tag("Gappein Exception:").w(exception, "Error setting documents: ")
        }
}

inline fun <reified T> QuerySnapshot.getObject(): List<T> {
    val data = this.toObjects(T::class.java)
    return data.toList()
}

inline fun DocumentReference.getRealtimeValue(
    crossinline returnResult: (DocumentSnapshot?, Exception?) -> Unit
) {
    this.addSnapshotListener { value, error ->
        if (error != null) {
            returnResult(null, error)
            return@addSnapshotListener
        }
        returnResult(value, null)
    }
}

