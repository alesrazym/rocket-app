package cz.quanti.razym.rocketapp.system

import android.icu.text.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.UiScreenState
import cz.quanti.razym.rocketapp.presentation.UiText
import cz.quanti.razym.rocketapp.ui.ContentStatusText
import cz.quanti.razym.rocketapp.ui.PreviewCommon.PREVIEW_WIDTH
import cz.quanti.razym.rocketapp.ui.RocketAppPreview
import cz.quanti.razym.rocketapp.ui.StateFullPullToRefresh
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
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.initialize()
        }

        RocketListScreen(
            uiState = uiState,
            onRefresh = viewModel::fetchRockets,
            onItemClick = onRocketItemClick,
        )
    }
}

@Composable
private fun RocketListScreen(
    uiState: UiScreenState<List<Rocket>>,
    onRefresh: () -> Unit,
    onItemClick: (Rocket) -> Unit = {},
) {

    // TODO Is this the only way to show toasts here? Make local composition? Or scaffold adds it by default?
    val snackbarHostState = remember { SnackbarHostState() }
    if (uiState is UiScreenState.Success) {
        if (uiState.errorMessage != UiText.Empty) {
            // Can get a string in @Composable only...
            val string = uiState.errorMessage.asString()
            LaunchedEffect(uiState) {
                snackbarHostState.showSnackbar(string)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentColor = RocketappTheme.colors.onBackground,
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = RocketappTheme.colors.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(RocketappTheme.dimens.extraLargePadding),
            ) {
                RocketListTitle(
                    text = R.string.rockets_title,
                )
                RocketListBox(
                    uiState = uiState,
                    onRefresh = onRefresh,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

@Composable
private fun RocketListTitle(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        style = RocketappTheme.typography.headline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = RocketappTheme.dimens.extraLargePadding),
        textAlign = Start,
        color = RocketappTheme.colors.onBackground,
    )
}

@Composable
private fun RocketListBox(
    uiState: UiScreenState<List<Rocket>>,
    onRefresh: () -> Unit,
    onItemClick: (Rocket) -> Unit = {},
) {
    StateFullPullToRefresh(
        uiState = uiState,
        onRefresh = onRefresh,
    ) {
        RocketList(
            rockets = (uiState as UiScreenState.Success).data,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun RocketList(rockets: List<Rocket>, onItemClick: (Rocket) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RocketappTheme.shapes.medium,
        color = RocketappTheme.colors.primaryContainer,
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
                        color = RocketappTheme.colors.background,
                        thickness = RocketappTheme.dimens.listItemDividerSize,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = RocketappTheme.dimens.extraLargePadding),
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
            .padding(
                horizontal = RocketappTheme.dimens.extraLargePadding,
                vertical = RocketappTheme.dimens.defaultPadding,
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = stringResource(R.string.rocket_icon_content_description),
            modifier = Modifier
                .size(RocketappTheme.dimens.listItemIconSize)
                .padding(RocketappTheme.dimens.smallPadding),
            tint = RocketappTheme.colors.primary,
        )

        Spacer(modifier = Modifier.width(RocketappTheme.dimens.defaultSpacerSize))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = rocket.name,
                style = RocketappTheme.typography.itemTitle,
                color = RocketappTheme.colors.onPrimaryContainer,
            )

//            Spacer(modifier = Modifier.height(RocketappTheme.dimens.smallSpacerSize))

            FirstFlightText(date = rocket.firstFlight)
        }

        Spacer(modifier = Modifier.width(RocketappTheme.dimens.defaultSpacerSize))

        Icon(
            painter = painterResource(
                id = R.drawable.baseline_arrow_forward_ios_24,
            ),
            contentDescription = stringResource(R.string.arrow_icon_content_description),
            modifier = Modifier.size(RocketappTheme.dimens.chevronItemIconSize),
            tint = RocketappTheme.colors.outline,
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
        style = RocketappTheme.typography.itemSubtitle.copy(
            color = RocketappTheme.colors.secondary,
        ),
    )
}

@RocketAppPreview
@Composable
private fun RocketListScreenPreview(
    @PreviewParameter(RocketsProvider::class) rockets: UiScreenState<List<Rocket>>,
) {
    RocketappTheme {
        RocketListScreen(
            uiState = rockets,
            onRefresh = { },
            onItemClick = { },
        )
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH, heightDp = 100)
@Composable
private fun RocketListItemPreview() {
    RocketappTheme {
        RocketListItem(
            rocket = previewRocket(),
            onClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH, heightDp = 100)
@Composable
private fun RocketListTextPreview() {
    RocketappTheme {
        ContentStatusText(UiText.StringResource(R.string.rockets_loading))
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH)
@Composable
private fun RocketListTitlePreview() {
    RocketappTheme {
        RocketListTitle(R.string.rockets_title)
    }
}

private class RocketsProvider : PreviewParameterProvider<UiScreenState<List<Rocket>>> {
    override val values =
        sequenceOf(
            UiScreenState.Success(
                data = previewRockets(4),
                refreshing = false,
            ),
            UiScreenState.Success(
                data = previewRockets(20),
                refreshing = false,
            ),
            UiScreenState.Success(
                data = previewRockets(4),
                refreshing = true,
            ),
            UiScreenState.Loading(
                message = UiText.StringResource(R.string.rockets_loading),
            ),
            UiScreenState.Error(
                errorMessage = UiText.StringResource(R.string.getting_rocket_detail_failed),
            ),
        )
}

private fun previewRockets(num: Int = 9) = List(num) {
    previewRocket(it)
}

private fun previewRocket(num: Int = 9) = Rocket(
    "Falcon $num",
    RocketData.firstFlightParser.parse("2010-06-04"),
    "falcon_$num",
)
