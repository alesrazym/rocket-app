package cz.quanti.razym.rocketapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
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

internal val lightModeColors by lazy {
    RocketappColors()
}

internal val darkModeColors by lazy {
    RocketappColors(
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
}
