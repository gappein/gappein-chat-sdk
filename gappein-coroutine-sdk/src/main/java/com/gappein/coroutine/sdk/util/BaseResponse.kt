package com.gappein.coroutine.sdk.util

data class BaseResponse<T>(val data: T? = null, val exception: Exception? = null)

data class BaseUnitResponse(val data: Unit? = null, val exception: Exception? = null)