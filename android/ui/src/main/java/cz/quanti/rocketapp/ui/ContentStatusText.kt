package cz.quanti.rocketapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cz.quanti.rocketapp.presentation.UiText
import cz.quanti.rocketapp.ui.theme.RocketAppTheme

@Composable
fun ContentStatusText(
    text: UiText,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Text(
        text = text.asString(),
        style = RocketAppTheme.typography.bodyLarge,
        modifier =
            modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .clickable { onClick() }
                .padding(RocketAppTheme.dimens.extraLargePadding),
        textAlign = TextAlign.Center,
        color = RocketAppTheme.colors.onBackground,
    )
}
