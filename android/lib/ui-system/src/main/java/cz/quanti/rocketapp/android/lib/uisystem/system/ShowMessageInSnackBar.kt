package cz.quanti.rocketapp.android.lib.uisystem.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiScreenState

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
