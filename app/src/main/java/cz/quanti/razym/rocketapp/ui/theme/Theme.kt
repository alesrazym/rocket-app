package cz.quanti.razym.rocketapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

private val lightModeColors by lazy {
    RocketappColors()
}

private val darkModeColors by lazy {
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

@Immutable
data class RocketappDimens(
    val zeroPadding: Dp = 0.dp,
    val smallPadding: Dp = 4.dp,
    val defaultPadding: Dp = 8.dp,
    val largePadding: Dp = 12.dp,
    val extraLargePadding: Dp = 16.dp,
    val chevronItemIconSize: Dp = 16.dp,
    val listItemIconSize: Dp = 32.dp,
    val navigationIconSize: Dp = 36.dp,
    val stageItemIconSize: Dp = 40.dp,
    val smallSpacerSize: Dp = 8.dp,
    val defaultSpacerSize: Dp = 16.dp,
    val parameterCardSize: Dp = 100.dp,
    val defaultCornerSize: Dp = 24.dp,
    val listItemDividerSize: Dp = 1.dp,
)

@Immutable
data class RocketAppTypography(
    // Copy from MaterialTheme (commented out not used in app now).
//    val displayLarge: TextStyle = Typography.displayLarge,
//    val displayMedium: TextStyle = Typography.displayMedium,
//    val displaySmall: TextStyle = Typography.displaySmall,
    val headlineLarge: TextStyle = Typography.headlineLarge,
    val headlineMedium: TextStyle = Typography.headlineMedium,
//    val headlineSmall: TextStyle = Typography.headlineSmall,
    val titleLarge: TextStyle = Typography.titleLarge,
    val titleMedium: TextStyle = Typography.titleMedium,
//    val titleSmall: TextStyle = Typography.titleSmall,
    val bodyLarge: TextStyle = Typography.bodyLarge,
    val bodyMedium: TextStyle = Typography.bodyMedium,
//    val bodySmall: TextStyle = Typography.bodySmall,
//    val labelLarge: TextStyle = Typography.labelLarge,
//    val labelMedium: TextStyle = Typography.labelMedium,
//    val labelSmall: TextStyle = Typography.labelSmall,

    // Custom (maybe FFU).
//    val title: TextStyle = TextStyle.Default,
//    val body: TextStyle = TextStyle.Default,
//    val label: TextStyle = TextStyle.Default,
)

@Immutable
class Shapes(
    // Copy from MaterialTheme (commented out not used in app now).
    // Shapes None and Full are omitted as None is a RectangleShape and Full is a CircleShape.
//    val extraSmall: CornerBasedShape = ShapeDefaults.ExtraSmall,
//    val small: CornerBasedShape = ShapeDefaults.Small,
    val medium: CornerBasedShape = ShapeDefaults.Medium,
//    val large: CornerBasedShape = ShapeDefaults.Large,
//    val extraLarge: CornerBasedShape = ShapeDefaults.ExtraLarge,
)
