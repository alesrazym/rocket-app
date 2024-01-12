package cz.quanti.common

import io.ktor.client.engine.ClientEngineClosedException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.SendCountExceedException
import io.ktor.client.plugins.contentnegotiation.ContentConverterException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    data class Error(val exception: Throwable? = null) : Result<Nothing>

    data object Loading : Result<Nothing>
}

// TODO: flow on ios?
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch {
            when (it) {
                is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException ->
                    emit(Result.Error(RocketException.NetworkException(it.message ?: "Network timeout", it)))
                is IOException ->
                    emit(Result.Error(RocketException.NetworkException(it.message ?: "Network error", it)))
                is ResponseException ->
                    emit(Result.Error(RocketException.HttpException(it.response.status, it)))
                is SendCountExceedException ->
                    emit(
                        Result.Error(
                            RocketException.NetworkException(it.message ?: "Infinite or too long redirect", it),
                        ),
                    )
                is ClientEngineClosedException ->
                    emit(Result.Error(RocketException.Exception(it.message ?: "Client engine closed", it)))
                is ContentConverterException ->
                    emit(Result.Error(RocketException.ContentException(it.message ?: "Content conversion error", it)))
                else -> emit(Result.Error(RocketException.Exception(it.message ?: "Unknown error", it)))
            }
        }
}
