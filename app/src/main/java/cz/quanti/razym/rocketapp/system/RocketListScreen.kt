@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package cz.quanti.razym.rocketapp.system

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.icu.text.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.ui.theme.LocalColors
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.androidx.compose.getViewModel
import java.util.Date
import java.util.Locale

private fun getFirstFlightFormat() = DateFormat.getDateInstance(
    DateFormat.MEDIUM,
    Locale.getDefault()
)

data object RocketListScreen : Screen("rocketList")

fun NavGraphBuilder.rocketListScreen(
    onRocketItemClick: (rocket: Rocket) -> Unit = {},
) {
    composable(RocketListScreen.route) {
        val viewModel: RocketListViewModel = getViewModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            // TODO this is not perfect condition, as after configuration change
            //  it will reload data if there has been an error and there are no rockets then.
            if (!uiState.loading && uiState.rockets == null) {
                viewModel.fetchRockets()
            }
        }

        RocketListScreen(
            refreshing = uiState.loading,
            rockets = uiState.rockets,
            onRefresh = viewModel::fetchRockets,
            onItemClick = onRocketItemClick,
        )
    }
}

@Composable
private fun RocketListScreen(
    refreshing: Boolean = false,
    rockets: List<Rocket>?,
    onRefresh: () -> Unit,
    onItemClick: (Rocket) -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = LocalColors.current.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            RocketListTitle(
                text = R.string.rockets_title,
            )
            RocketListBox(
                rockets = rockets,
                refreshing = refreshing,
                onRefresh = onRefresh,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun RocketListTitle(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = Start,
        color = LocalColors.current.onBackground,
    )
}

@Composable
private fun RocketListBox(
    rockets: List<Rocket>?,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Rocket) -> Unit = {},
) {
    val state = rememberPullRefreshState(refreshing, onRefresh)

    Box(
        Modifier
            .pullRefresh(state)
            .fillMaxSize()
    ) {
        if (rockets == null && refreshing) {
            ContentStatusText(
                text = R.string.rockets_loading,
            )
        } else {
            if (rockets == null)
                ContentStatusText(
                    text = R.string.rockets_loading_error,
                    onClick = onRefresh,
                )
            else
                RocketList(
                    rockets = rockets,
                    onItemClick = onItemClick,
                )
        }

        // TODO handle error messages

        // TODO update to material3 once 1.2 with pull to refresh released
        // https://developer.android.com/jetpack/androidx/releases/compose-material3#version_12_2
        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun RocketList(rockets: List<Rocket>, onItemClick: (Rocket) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = LocalColors.current.primaryContainer,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            itemsIndexed(
                items = rockets,
                key = { _, rocket -> rocket.id },
            ) { index, rocket ->
                RocketListItem(
                    rocket = rocket,
                    onClick = onItemClick,
                )

                if (index < rockets.lastIndex)
                    // Will be HorizontalDivider when available
                    Divider(
                        color = LocalColors.current.background,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                    )
            }
        }
    }
}

@Composable
private fun RocketListItem(rocket: Rocket, onClick: (Rocket) -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            // Note that order matters, so apply click before padding.
            .clickable { onClick(rocket) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = stringResource(R.string.rocket_icon_content_description),
            modifier = Modifier.size(32.dp),
            tint = LocalColors.current.primary,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = rocket.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = LocalColors.current.onPrimaryContainer,
            )

//            Spacer(modifier = Modifier.height(8.dp))

            FirstFlightText(date = rocket.firstFlight)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(
                id = R.drawable.baseline_arrow_forward_ios_24,
            ),
            contentDescription = stringResource(R.string.arrow_icon_content_description),
            modifier = Modifier.size(16.dp),
            tint = LocalColors.current.outline,
        )
    }
}

@Composable
private fun FirstFlightText(date: Date?) {
    val str = if (date == null) {
        stringResource(
            R.string.first_flight,
            stringResource(R.string.first_flight_unknown)
        )
    } else {
        stringResource(
            R.string.first_flight,
            getFirstFlightFormat().format(date)
        )
    }

    Text(
        text = str,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = LocalColors.current.secondary,
        ),
    )
}

@Preview(
    showBackground = true,
    widthDp = previewWidth,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark",
    heightDp = 600,
)
@Preview(showBackground = true, widthDp = previewWidth, heightDp = 600)
@Composable
private fun RocketListScreenLessItemsPreview() {
    RocketListScreenPreview(previewRockets(4))
}

@Preview(showBackground = true, widthDp = previewWidth, heightDp = 600)
@Composable
private fun RocketListScreenMoreItemsPreview() {
    RocketListScreenPreview(previewRockets(20))
}

@Composable
private fun RocketListScreenPreview(rockets: List<Rocket>) {
    RocketappTheme {
        RocketListScreen(
            refreshing = false,
            rockets = rockets,
            onRefresh = { },
            onItemClick = { },
        )
    }
}

@Preview(showBackground = true, widthDp = previewWidth, heightDp = 600)
@Composable
private fun RocketListScreenLoadingPreview() {
    RocketappTheme {
        RocketListScreen(
            refreshing = true,
            rockets = null,
            onRefresh = { },
            onItemClick = { },
        )
    }
}

@Preview(showBackground = true, widthDp = previewWidth, heightDp = 100)
@Composable
private fun RocketListItemPreview() {
    RocketappTheme {
        RocketListItem(
            rocket = previewRocket(),
            onClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = previewWidth, heightDp = 100)
@Composable
private fun RocketListTextPreview() {
    RocketappTheme {
        ContentStatusText(R.string.rockets_loading)
    }
}

@Preview(showBackground = true, widthDp = previewWidth)
@Composable
private fun RocketListTitlePreview() {
    RocketappTheme {
        RocketListTitle(R.string.rockets_title)
    }
}

private fun previewRockets(num: Int = 9) = List(num) {
    previewRocket(it)
}

private fun previewRocket(num: Int = 9) = Rocket(
    "Falcon $num",
    RocketData.firstFlightParser.parse("2010-06-04"),
    "falcon_$num",
)

const val previewWidth = 375
