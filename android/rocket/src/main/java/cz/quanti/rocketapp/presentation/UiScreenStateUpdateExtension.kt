package cz.quanti.rocketapp.presentation

import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.common.Result
import cz.quanti.common.ResultException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

suspend fun <T, S> MutableStateFlow<UiScreenState<S>>.update(
    resultFlow: Flow<Result<T>>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = defaultErrorTransform(),
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
) {
    resultFlow.collect { result ->
        this.update(result, transform, errorTransform, loadingMessage)
    }
}

fun <T, S> MutableStateFlow<UiScreenState<S>>.update(
    result: Result<T>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = defaultErrorTransform(),
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
) {
    this.update {
        it.update(result, transform, errorTransform, loadingMessage)
    }
}

fun <S> MutableStateFlow<UiScreenState<S>>.loading(loadingMessage: UiText = UiText.StringResource(R.string.loading)) {
    this.update {
        it.loading(loadingMessage)
    }
}

fun <T, S> UiScreenState<S>.update(
    result: Result<T>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = defaultErrorTransform(),
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
): UiScreenState<S> {
    return when (result) {
        is Result.Loading -> loading(loadingMessage)

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

fun <S> UiScreenState<S>.loading(loadingMessage: UiText = UiText.StringResource(R.string.loading)): UiScreenState<S> {
    return if (this is UiScreenState.Data) {
        this.copy(
            refreshing = true,
        )
    } else {
        UiScreenState.Loading(message = loadingMessage)
    }
}

private fun defaultErrorTransform(): (Throwable?) -> UiText =
    {
        when (it) {
            is ResultException.NetworkException -> UiText.StringResource(R.string.error_io)
            is ResultException.HttpException -> UiText.StringResource(R.string.error_server_response)
            is ResultException.ContentException -> UiText.StringResource(R.string.error_json)
            is ResultException.Exception -> UiText.StringResource(R.string.unknown_error)
            else -> UiText.StringResource(R.string.unknown_error)
        }
    }
