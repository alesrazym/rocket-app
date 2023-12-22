package cz.quanti.rocketapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object RocketappTheme {
    val colors: RocketappColors
        @Composable
        get() = LocalColors.current
    val dimens: RocketappDimens
        @Composable
        get() = LocalDimensions.current
    val typography: RocketAppTypography
        @Composable
        get() = LocalTypography.current
    val shapes: Shapes
        @Composable
        get() = LocalShapes.current
}

@Composable
fun RocketappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkModeColors else lightModeColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            // TODO this works quite opposite, don't know why.
            //  Tested on 3 emulators, API 26, 31, 34
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimensions provides RocketappDimens(),
        LocalTypography provides RocketAppTypography(),
        LocalShapes provides Shapes(),
        content = content
    )
}

private val LocalColors = staticCompositionLocalOf {
    lightModeColors
}

private val LocalDimensions = staticCompositionLocalOf {
    RocketappDimens()
}

private val LocalTypography = staticCompositionLocalOf {
    RocketAppTypography()
}

private val LocalShapes = staticCompositionLocalOf {
    Shapes()
}
