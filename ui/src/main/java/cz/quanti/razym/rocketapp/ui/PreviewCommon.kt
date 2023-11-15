package cz.quanti.razym.rocketapp.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import cz.quanti.razym.rocketapp.ui.PreviewCommon.PREVIEW_HEIGHT
import cz.quanti.razym.rocketapp.ui.PreviewCommon.PREVIEW_WIDTH

object PreviewCommon {
    const val PREVIEW_WIDTH = 375
    const val PREVIEW_HEIGHT = 600
}

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Preview(
    showBackground = true,
    widthDp = PREVIEW_WIDTH,
    heightDp = PREVIEW_HEIGHT,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light",
)
annotation class RocketAppPreviewLight

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Preview(
    showBackground = true,
    widthDp = PREVIEW_WIDTH,
    heightDp = PREVIEW_HEIGHT,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark",
)
annotation class RocketAppPreviewDark

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@RocketAppPreviewLight
@RocketAppPreviewDark
annotation class RocketAppPreview
