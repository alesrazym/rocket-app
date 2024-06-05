package cz.quanti.rocketapp.system

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
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.presentation.RocketListViewModel
import cz.quanti.rocketapp.presentation.RocketUiState
import cz.quanti.rocketapp.presentation.UiScreenState
import cz.quanti.rocketapp.presentation.UiText
import cz.quanti.rocketapp.presentation.asFirstFlightUiText
import cz.quanti.rocketapp.presentation.asUiText
import cz.quanti.rocketapp.system.PreviewCommon.PREVIEW_WIDTH
import cz.quanti.rocketapp.system.theme.RocketAppTheme
import cz.quanti.rocketapp.util.toDate
import org.koin.androidx.compose.koinViewModel

data object RocketListScreen : RocketAppScreen("rocketList")

fun NavGraphBuilder.rocketListScreen(
    onRocketItemClick: (rocket: RocketUiState, name: String) -> Unit = { _, _ -> },
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
    uiState: UiScreenState<List<RocketUiState>>,
    onRefresh: () -> Unit,
    onItemClick: (rocket: RocketUiState, name: String) -> Unit = { _, _ -> },
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
    uiState: UiScreenState<List<RocketUiState>>,
    onRefresh: () -> Unit,
    onItemClick:  (rocket: RocketUiState, name: String) -> Unit = { _, _ -> },
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
private fun RocketList(
    rockets: List<RocketUiState>,
    onItemClick:  (rocket: RocketUiState, name: String) -> Unit = { _, _ -> },
) {
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
private fun RocketListItem(
    rocket: RocketUiState,
    onClick:  (rocket: RocketUiState, name: String) -> Unit = { _, _ -> },
) {
    val name = rocket.name.asString()
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            // Note that order matters, so apply click before padding.
            .clickable { onClick(rocket, name) }
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
                text = rocket.name.asString(),
                style = RocketAppTheme.typography.itemTitle,
                color = RocketAppTheme.colors.onPrimaryContainer,
            )

//            Spacer(modifier = Modifier.height(RocketAppTheme.dimens.smallSpacerSize))

            Text(
                text = rocket.firstFlight.asString(),
                style = RocketAppTheme.typography.itemSubtitle.copy(
                    color = RocketAppTheme.colors.secondary,
                ),
            )
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

@PreviewRocketApp
@Composable
private fun RocketListScreenPreview(
    @PreviewParameter(RocketsProvider::class) rockets: UiScreenState<List<RocketUiState>>,
) {
    RocketAppTheme {
        RocketListScreen(
            uiState = rockets,
            onRefresh = { },
            onItemClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, widthDp = PREVIEW_WIDTH, heightDp = 100)
@Composable
private fun RocketListItemPreview() {
    RocketAppTheme {
        RocketListItem(
            rocket = previewRocket(),
            onClick = { _, _ -> },
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

private class RocketsProvider : PreviewParameterProvider<UiScreenState<List<RocketUiState>>> {
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

private fun previewRocket(num: Int = 9) = RocketUiState(
    "Falcon $num".asUiText(),
    "2010-06-04".toDate().asFirstFlightUiText(),
    "falcon_$num",
)
