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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.UiScreenState
import cz.quanti.razym.rocketapp.presentation.UiText
import cz.quanti.razym.rocketapp.ui.ContentStatusText
import cz.quanti.razym.rocketapp.ui.PreviewCommon.PREVIEW_WIDTH
import cz.quanti.razym.rocketapp.ui.PreviewRocketApp
import cz.quanti.razym.rocketapp.ui.StateFullPullToRefresh
import cz.quanti.razym.rocketapp.ui.theme.RocketAppTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import java.util.Locale
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate

private fun getFirstFlightFormat() = DateFormat.getDateInstance(
    DateFormat.MEDIUM,
    Locale.getDefault()
)

data object RocketListScreen : Screen("rocketList")

fun NavGraphBuilder.rocketListScreen(
    onRocketItemClick: (rocket: Rocket) -> Unit = {},
) {
    composable(RocketListScreen.route) {
        val viewModel: RocketListViewModel = koinViewModel()
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
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = RocketAppTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(RocketAppTheme.dimens.extraLargePadding),
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

@Composable
private fun RocketListTitle(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        style = RocketAppTheme.typography.headline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = RocketAppTheme.dimens.extraLargePadding),
        textAlign = Start,
        color = RocketAppTheme.colors.onBackground,
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
            rockets = it,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun RocketList(rockets: List<Rocket>, onItemClick: (Rocket) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RocketAppTheme.shapes.medium,
        color = RocketAppTheme.colors.primaryContainer,
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
                        color = RocketAppTheme.colors.background,
                        thickness = RocketAppTheme.dimens.listItemDividerSize,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = RocketAppTheme.dimens.extraLargePadding),
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
                horizontal = RocketAppTheme.dimens.extraLargePadding,
                vertical = RocketAppTheme.dimens.defaultPadding,
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = stringResource(R.string.rocket_icon_content_description),
            modifier = Modifier
                .size(RocketAppTheme.dimens.listItemIconSize)
                .padding(RocketAppTheme.dimens.smallPadding),
            tint = RocketAppTheme.colors.primary,
        )

        Spacer(modifier = Modifier.width(RocketAppTheme.dimens.defaultSpacerSize))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = rocket.name,
                style = RocketAppTheme.typography.itemTitle,
                color = RocketAppTheme.colors.onPrimaryContainer,
            )

//            Spacer(modifier = Modifier.height(RocketAppTheme.dimens.smallSpacerSize))

            FirstFlightText(date = rocket.firstFlight)
        }

        Spacer(modifier = Modifier.width(RocketAppTheme.dimens.defaultSpacerSize))

        Icon(
            painter = painterResource(
                id = R.drawable.baseline_arrow_forward_ios_24,
            ),
            contentDescription = stringResource(R.string.arrow_icon_content_description),
            modifier = Modifier.size(RocketAppTheme.dimens.chevronItemIconSize),
            tint = RocketAppTheme.colors.outline,
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
        style = RocketAppTheme.typography.itemSubtitle.copy(
            color = RocketAppTheme.colors.secondary,
        ),
    )
}

@PreviewRocketApp
@Composable
private fun RocketListScreenPreview(
    @PreviewParameter(RocketsProvider::class) rockets: UiScreenState<List<Rocket>>,
) {
    RocketAppTheme {
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
    RocketAppTheme {
        RocketListItem(
            rocket = previewRocket(),
            onClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH, heightDp = 100)
@Composable
private fun RocketListTextPreview() {
    RocketAppTheme {
        ContentStatusText(UiText.StringResource(R.string.rockets_loading))
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH)
@Composable
private fun RocketListTitlePreview() {
    RocketAppTheme {
        RocketListTitle(R.string.rockets_title)
    }
}

private class RocketsProvider : PreviewParameterProvider<UiScreenState<List<Rocket>>> {
    override val values =
        sequenceOf(
            UiScreenState.Data(
                data = previewRockets(4),
                refreshing = false,
            ),
            UiScreenState.Data(
                data = previewRockets(20),
                refreshing = false,
            ),
            UiScreenState.Data(
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
    // TODO temporary java Date, maybe incorrect zone convert...
    Date("2010-06-04".toLocalDate().atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()),
    "falcon_$num",
)
