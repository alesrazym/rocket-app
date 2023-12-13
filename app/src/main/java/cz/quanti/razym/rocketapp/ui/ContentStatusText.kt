package cz.quanti.razym.rocketapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme

@Composable
fun ContentStatusText(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Text(
        text = stringResource(text),
        style = RocketappTheme.typography.bodyLarge,
        modifier =
            modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .clickable { onClick() }
                .padding(RocketappTheme.dimens.extraLargePadding),
        textAlign = TextAlign.Center,
        color = RocketappTheme.colors.onBackground,
    )
}
