package com.gappein.coroutine.sdk.util

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> Continuation<T>.resumeNullable(value: T?) {
    if (value != null) {
        this.resume(value)
    }
}

fun <T> Continuation<T>.resumeWithExceptionNullable(value: Exception?) {
    if (value != null) {
        this.resumeWithException(value)
    }
}