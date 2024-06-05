package cz.quanti.rocketapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cz.quanti.rocketapp.system.LocalSnackbar

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
    data class Data<T>(
        val data: T,
        val refreshing: Boolean = false,
        val errorMessage: UiText = UiText.Empty,
    ) : UiScreenState<T>

    data class Error(
        val errorMessage: UiText = UiText.Empty,
    ) : UiScreenState<Nothing>
}

/**
 * Show message in snackbar.
 * As per design rules, @Compose function should not result in "Nothing",
 * so the caller is responsible for checking there is a message to show.
 */
@Composable
fun UiScreenState.Data<*>.ShowMessageInSnackBar() {
    val string = this.errorMessage.asString()
    val provider = LocalSnackbar.current

    LaunchedEffect(this) {
        provider.showSnackbar(string)
    }
}

@Composable
fun UiScreenState.Loading.ShowMessageInSnackBar() {
    val string = this.message.asString()
    val provider = LocalSnackbar.current

    LaunchedEffect(this) {
        provider.showSnackbar(string)
    }
}

@Composable
fun UiScreenState.Error.ShowMessageInSnackBar() {
    val string = this.errorMessage.asString()
    val provider = LocalSnackbar.current

    LaunchedEffect(this) {
        provider.showSnackbar(string)
    }
}
