package cz.quanti.rocketapp.presentation

import android.util.MalformedJsonException
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketropository.Result
import java.io.IOException
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

suspend fun <T, S> Flow<Result<T>>.update(
    uiState: MutableStateFlow<UiScreenState<S>>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = defaultErrorTransform(),
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
) {
    this.collect { result ->
        uiState.update {
            it.update(result, transform, errorTransform, loadingMessage)
        }
    }
}

fun <T, S> UiScreenState<S>.update(
    result: Result<T>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = defaultErrorTransform(),
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
): UiScreenState<S> {
    return when (result) {
        is Result.Loading ->
            if (this is UiScreenState.Data) {
                this.copy(
                    refreshing = true,
                )
            } else {
                UiScreenState.Loading(message = loadingMessage)
            }

        is Result.Success<T> ->
            // Clear all messages now.
            UiScreenState.Data(
                data = transform(result.data),
            )

        is Result.Error ->
            if (this is UiScreenState.Data) {
                this.copy(
                    refreshing = false,
                    errorMessage = errorTransform(result.exception),
                )
            } else {
                UiScreenState.Error(
                    errorMessage = errorTransform(result.exception),
                )
            }
    }
}

private fun defaultErrorTransform(): (Throwable?) -> UiText =
    {
        when (it) {
            // TODO: What exceptions can ktor (and it's respective engines) throw?
            // is JsonDataException -> UiText.StringResource(R.string.error_json)
            is MalformedJsonException -> UiText.StringResource(R.string.error_json)
            is TimeoutException -> UiText.StringResource(R.string.error_timeout)
            // is HttpException -> UiText.StringResource(R.string.error_server_response)
            is IOException -> UiText.StringResource(R.string.error_io)
            else -> UiText.StringResource(R.string.unknown_error)
        }
    }
