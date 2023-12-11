package cz.quanti.razym.rocketapp.presentation

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * A sealed class representing different types of UI text.
 * This class is used to encapsulate the logic for
 * displaying different types of text resources in the UI.
 */
sealed class UiText {
    // Just in case or example usage, DynamicString should not be used in production.
    data class DynamicString(
        val value: String,
    ) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    class PluralResource(
        @PluralsRes val resId: Int,
        val count: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            is PluralResource -> pluralStringResource(resId, count, *args)
        }
    }
}
