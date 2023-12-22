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
    data object Empty : UiText()

    // Just in case or example usage, DynamicString should not be used in production.
    data class DynamicString(
        val value: String,
    ) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (resId != other.resId) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    class PluralResource(
        @PluralsRes val resId: Int,
        val count: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralResource

            if (resId != other.resId) return false
            if (count != other.count) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + count
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    // TODO: This may be tested.
    @Composable
    fun asString(): String {
        return when (this) {
            Empty -> {
                ""
            }
            is DynamicString -> {
                value
            }
            is StringResource -> {
                stringResource(resId, *processArgs(args))
            }
            is PluralResource -> {
                pluralStringResource(resId, count, *processArgs(args))
            }
        }
    }

    @Composable
    private fun processArgs(args: Array<out Any>): Array<out Any> {
        if (args.any { it is UiText }) {
            return args.map { arg ->
                if (arg is UiText) arg.asString() else arg
            }.toTypedArray()
        }

        return args
    }
}

fun String?.asUiText(): UiText {
    return if (this.isNullOrEmpty()) {
        UiText.Empty
    } else {
        UiText.DynamicString(this)
    }
}
