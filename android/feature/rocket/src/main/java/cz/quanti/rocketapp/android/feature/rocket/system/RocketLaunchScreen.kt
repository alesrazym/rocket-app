package cz.quanti.rocketapp.android.feature.rocket.system

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.quanti.rocketapp.android.lib.uisystem.system.TopBarWithBackIcon
import cz.quanti.rocketapp.android.lib.uisystem.system.theme.RocketAppTheme
import cz.quanti.rocketapp.android.rocket.R

data object RocketLaunchScreen : RocketAppScreen("rocketLaunch")

fun NavGraphBuilder.rocketLaunchScreen(
    onBackClick: () -> Unit = {},
) {
    composable(RocketLaunchScreen.route) {
        RocketLaunchScreen(
            onBackClick = onBackClick,
        )
    }
}

@Preview
@Composable
fun RocketLaunchScreen(
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                title = stringResource(R.string.launch),
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Rocket Launch",
                style = RocketAppTheme.typography.topBar,
            )
        }
    }
}
