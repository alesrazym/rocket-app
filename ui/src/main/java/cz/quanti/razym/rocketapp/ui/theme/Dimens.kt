package cz.quanti.razym.rocketapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
