package cz.quanti.razym.rocketapp.system

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.quanti.razym.rocketapp.ui.theme.LocalColors

@Composable
fun ContentStatusText(@StringRes text: Int, onClick: () -> Unit = { }) {
    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .clickable { onClick() }
            .padding(16.dp),
        textAlign = TextAlign.Center,
        color = LocalColors.current.onBackground,
    )
}