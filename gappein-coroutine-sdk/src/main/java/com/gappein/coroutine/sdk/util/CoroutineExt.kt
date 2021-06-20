package com.gappein.coroutine.sdk.util

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

fun <T> Continuation<T>.resumeNullable(value: T?) {
    if (value != null) {
        this.resume(value)
    }
}