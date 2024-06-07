package com.shubh.news

sealed class NewsResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T, message: String? = null) : NewsResult<T>(data, message)
    class Error<T>(data: T? = null, message: String) : NewsResult<T>(data, message)
    class Loading<T> : NewsResult<T>()
}