@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.rocketapp.ui

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
import cz.quanti.rocketapp.R
import cz.quanti.rocketapp.ui.theme.RocketAppTheme

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
                style = RocketAppTheme.typography.topBar,
                color = RocketAppTheme.colors.onBackground,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RocketAppTheme.colors.background,
            scrolledContainerColor = RocketAppTheme.colors.background,
            navigationIconContentColor = RocketAppTheme.colors.onBackground,
            // TODO why does navigation content color works and title and action does not?
            titleContentColor = RocketAppTheme.colors.onBackground,
            actionIconContentColor = RocketAppTheme.colors.actionItem,
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
            .size(RocketAppTheme.dimens.navigationIconSize)
            .clickable { onBackClick() }
            .padding(RocketAppTheme.dimens.defaultPadding)
        ,
        painter = painterResource(id = R.drawable.ic_arrow_back),
        contentDescription = stringResource(R.string.back_to_list_back_arrow),
    )
}

