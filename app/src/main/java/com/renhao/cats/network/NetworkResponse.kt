package com.renhao.cats.network

import retrofit2.Response

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T?): NetworkResponse<T>()
    data class Error<T>(val errorCode: Int, val errorMessage: String): NetworkResponse<T>()

    companion object {
        fun <T> create(response: Response<T>): NetworkResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Success(it)
                } ?: Error(response.code(), "successful response, null body")
            } else {
                val msg = response.errorBody()?.string()
                val errorMessage = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                Error(response.code(), errorMessage ?: "Unknown error")
            }
        }
    }
}
