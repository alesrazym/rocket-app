package cz.quanti.common

import io.ktor.client.engine.ClientEngineClosedException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.SendCountExceedException
import io.ktor.client.plugins.contentnegotiation.ContentConverterException
import io.ktor.utils.io.errors.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    data class Error(val exception: ResultException? = null) : Result<Nothing>

    data object Loading : Result<Nothing>
}

@Suppress("detekt.TooGenericExceptionCaught")
suspend fun <T> asResult(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        // Not re-throw cancellation exception will result in behaviour,
        // that caller coroutine will not be cancelled!
        // See cancelExceptionThrowingFlow() for more info.
        e.asResult()
    }
}

// TODO: flow on ios?
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch {
            emit(it.asResult())
        }
}

private fun Throwable.asResult(): Result.Error {
    return Result.Error(
        when (this) {
            is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException ->
                RocketException.NetworkException(message ?: "Network timeout", this)

            is IOException ->
                RocketException.NetworkException(message ?: "Network error", this)

            is ResponseException ->
                RocketException.HttpException(response.status, this)

            is SendCountExceedException ->
                RocketException.NetworkException(message ?: "Infinite or too long redirect", this)

            is ClientEngineClosedException ->
                RocketException.Exception(message ?: "Client engine closed", this)

            is ContentConverterException ->
                RocketException.ContentException(message ?: "Content conversion error", this)

            is CancellationException ->
                RocketException.CanceledByUserException(message ?: "Canceled by user")

            else -> RocketException.Exception(message ?: "Unknown error", this)
        }
    )
}
