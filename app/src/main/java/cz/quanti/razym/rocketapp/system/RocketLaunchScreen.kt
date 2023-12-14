@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.razym.rocketapp.system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme

data object RocketLaunchScreen : Screen("rocketLaunch")

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
            RocketLaunchFragmentTopBar(
                title = stringResource(R.string.launch),
                onBackClick = onBackClick,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Rocket Launch",
                style = RocketappTheme.typography.topBar,
            )
        }
    }
}

@Composable
private fun RocketLaunchFragmentTopBar(
    title: String,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = RocketappTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(RocketappTheme.dimens.navigationIconSize)
                    .padding(RocketappTheme.dimens.defaultPadding)
                    .clickable { onBackClick() },
                painter = painterResource(id = R.drawable.ic_arrow_back),
                // TODO
                contentDescription = "",
            )
        },
    )
}
