package cz.quanti.rocketapp.android.lib.uisystem.presentation

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
