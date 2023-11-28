package cz.quanti.razym.rocketapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
    ) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val LocalColors = staticCompositionLocalOf {
    lightModeColors
}

data class RocketappColors(
    val background: Color = Color(0xFFF5F5F5),
    val onBackground: Color = Color.Black,
    val surface: Color = Color.White,
    val onSurface: Color = Color.Black,
    val primary: Color = Color(0xFFF25187),
    val onPrimary: Color = Color.White,
    val primaryContainer: Color = Color.White,
    val onPrimaryContainer: Color = Color.Black,
    val secondary: Color = Color(0xFFB8B8BB),
    val secondaryContainer: Color = Color(0xFFF5F5F5),
    val onSecondaryContainer: Color = Color.Black,
    val outline: Color = Color(0xFFA1A1A5),
    val actionItem: Color = Color(0xFF1F89FE)
)

private val lightModeColors = RocketappColors(
)

private val darkModeColors = RocketappColors(
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),
    primary = Color(0xFFF25187),
    primaryContainer = Color.Black,
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF121212),
    onSecondaryContainer = Color.White,
)
