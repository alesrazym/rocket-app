package cz.quanti.rocketapp.multiplatform.lib.common.infrastructure

import cz.quanti.rocketapp.multiplatform.lib.common.model.Result
import cz.quanti.rocketapp.multiplatform.lib.common.model.ResultException
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

@Suppress("LongMethod")
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch {
            when (it) {
                is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException ->
                    emit(
                        Result.Error(
                            ResultException.NetworkException(
                                it.message ?: "Network timeout", it,
                            ),
                        ),
                    )

                is IOException ->
                    emit(
                        Result.Error(
                            ResultException.NetworkException(
                                it.message ?: "Network error", it,
                            ),
                        ),
                    )

                is ResponseException ->
                    emit(Result.Error(ResultException.HttpException(it.response.status, it)))

                is SendCountExceedException ->
                    emit(
                        Result.Error(
                            ResultException.NetworkException(
                                it.message ?: "Infinite or too long redirect", it,
                            ),
                        ),
                    )

                is ClientEngineClosedException ->
                    emit(
                        Result.Error(
                            ResultException.Exception(
                                it.message ?: "Client engine closed", it,
                            ),
                        ),
                    )

                is ContentConverterException ->
                    emit(
                        Result.Error(
                            ResultException.ContentException(
                                it.message ?: "Content conversion error", it,
                            ),
                        ),
                    )

                else -> emit(
                    Result.Error(
                        ResultException.Exception(
                            it.message ?: "Unknown error",
                            it,
                        ),
                    ),
                )
            }
        }
}
