package cz.quanti.razym.rocketapp.presentation

import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base class for handling with UI Screen state in cases,\
 * where there are data fetched from the server and there is \
 * also a need to handle refreshing and error states.
 */
sealed interface UiScreenState<out T> {

    /**
     * There are no items yet, initial loading in progress.
     */
    data class Loading(
        val message: UiText = UiText.Empty,
    ) : UiScreenState<Nothing>

    /**
     * We have loaded items successfully.
     * In addition, refresh may be performed. In this case, [refreshing] is true.
     * If refreshing fails, [errorMessage] will contain error message.
     */
    data class Success<T>(
        val data: T,
        val refreshing: Boolean = false,
        val errorMessage: UiText = UiText.Empty,
    ) : UiScreenState<T>

    data class Error(
        val errorMessage: UiText = UiText.Empty,
    ) : UiScreenState<Nothing>
}

suspend fun <T, S> Flow<Result<T>>.update(
    uiState: MutableStateFlow<UiScreenState<S>>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = { UiText.StringResource(R.string.unknown_error) },
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
) {
    this.collect {result ->
        uiState.update {
            it.update(result, transform, errorTransform, loadingMessage)
        }
    }
}

fun <T, S> UiScreenState<S>.update(
    result: Result<T>,
    transform: (T) -> S,
    errorTransform: (Throwable?) -> UiText = { UiText.StringResource(R.string.unknown_error) },
    loadingMessage: UiText = UiText.StringResource(R.string.loading),
) : UiScreenState<S> {
    return when (result) {
        is Result.Loading ->
            if (this is UiScreenState.Success) {
                this.copy(
                    refreshing = true,
                )
            } else {
                UiScreenState.Loading(message = loadingMessage)
            }

        is Result.Success<T> ->
            // Clear all messages now.
            UiScreenState.Success(
                data = transform(result.data),
            )

        is Result.Error ->
            if (this is UiScreenState.Success) {
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
