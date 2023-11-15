package cz.quanti.razym.rocketapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class RocketAppTypography(
    // TODO set explicitly?
    //        fontFamily = null, // FontFamily.SansSerif etc
    //        lineHeight = TextUnit.Unspecified,
    //        letterSpacing = TextUnit.Unspecified,

    // Sort by generic/specialized, then by size, then by weight.
    val headline: TextStyle = TextStyle(
        fontSize = 38.sp,
        fontWeight = FontWeight.Bold,
    ),
    val titleLarge: TextStyle = TextStyle(
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    val topBar: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
    ),
    val cardParameterValue: TextStyle = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    val cardParameter: TextStyle = TextStyle(
        fontSize = 19.sp,
        fontWeight = FontWeight.Normal,
    ),
    val itemTitle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    val itemSubtitle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
    ),
)
