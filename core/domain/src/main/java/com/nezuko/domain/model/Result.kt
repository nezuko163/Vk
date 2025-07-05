package com.nezuko.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class Result<T> {
    class Loading<T> : Result<T>()
    class None<T> : Result<T>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Throwable) : Result<T>()
}

inline fun <T> resultFlow(crossinline block: suspend () -> T): Flow<Result<T>> = flow {
    emit(Result.Loading<T>())
    try {
        emit(Result.Success(block()))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}

inline fun <T, R> Flow<Result<T>>.asd(
    crossinline block: suspend (T) -> R
): Flow<Result<R>> =
    this.map { res ->
        when (res) {
            is Result.Error   ->
                Result.Error<R>(res.exception)
            is Result.Loading ->
                Result.Loading<R>()
            is Result.None    ->
                Result.None<R>()
            is Result.Success -> {
                try {
                    val newData = block(res.data)
                    Result.Success(newData)
                } catch (e: Throwable) {
                    Result.Error<R>(e)
                }
            }
        }
    }