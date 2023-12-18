@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.razym.rocketapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme

@Composable
fun TopBar(
    title: String,
    navigationIcon: @Composable () -> Unit = { },
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = RocketappTheme.typography.topBar,
                color = RocketappTheme.colors.onBackground,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RocketappTheme.colors.background,
            scrolledContainerColor = RocketappTheme.colors.background,
            navigationIconContentColor = RocketappTheme.colors.onBackground,
            // TODO why does navigation content color works and title and action does not?
            titleContentColor = RocketappTheme.colors.onBackground,
            actionIconContentColor = RocketappTheme.colors.actionItem,
        ),
    )
}

@Composable
fun TopBarWithBackIcon(
    title: String,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopBar(
        title = title,
        navigationIcon = {
            BackIcon(
                onBackClick = onBackClick,
            )
        },
        actions = actions,
    )
}

@Composable
fun BackIcon(
    onBackClick: () -> Unit,
) {
    Icon(
        modifier = Modifier
            .size(RocketappTheme.dimens.navigationIconSize)
            .clickable { onBackClick() }
            .padding(RocketappTheme.dimens.defaultPadding)
        ,
        painter = painterResource(id = R.drawable.ic_arrow_back),
        contentDescription = stringResource(R.string.back_to_list_back_arrow),
    )
}