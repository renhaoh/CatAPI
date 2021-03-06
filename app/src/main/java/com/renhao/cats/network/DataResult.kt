package com.renhao.cats.network

sealed class DataResult<T>() {
    data class Success<T>(val data: T): DataResult<T>()
    data class Error<T>(val messageId: Int?): DataResult<T>()
    class Loading<T>(): DataResult<T>()
}
