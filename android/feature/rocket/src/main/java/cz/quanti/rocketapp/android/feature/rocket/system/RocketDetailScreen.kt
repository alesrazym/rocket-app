package cz.quanti.rocketapp.android.feature.rocket.system

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.quanti.rocketapp.android.feature.rocket.presentation.RocketDetailUiState
import cz.quanti.rocketapp.android.feature.rocket.presentation.RocketDetailViewModel
import cz.quanti.rocketapp.android.feature.rocket.presentation.StageUiState
import cz.quanti.rocketapp.android.feature.rocket.presentation.previewStageUiState
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiScreenState
import cz.quanti.rocketapp.android.lib.uisystem.system.PreviewRocketApp
import cz.quanti.rocketapp.android.lib.uisystem.system.StateFullPullToRefresh
import cz.quanti.rocketapp.android.lib.uisystem.system.TopBarWithBackIcon
import cz.quanti.rocketapp.android.lib.uisystem.system.theme.RocketAppTheme
import cz.quanti.rocketapp.android.rocket.R
import org.koin.androidx.compose.koinViewModel

private const val ROCKET_DETAIL = "rocketDetail"
private const val ROCKET_ID = "rocketId"
private const val ROCKET_NAME = "rocketName"

data object RocketDetailScreen : RocketAppScreen(
    route = "$ROCKET_DETAIL/{$ROCKET_ID}?rocketName={$ROCKET_NAME}",
    navArguments = listOf(
        navArgument(ROCKET_ID) { type = NavType.StringType },
        navArgument(ROCKET_NAME) {
            type = NavType.StringType
            nullable = true
        },
    ),
) {
    fun createRoute(rocketId: String, rocketName: String? = null) =
        "$ROCKET_DETAIL/$rocketId?rocketName=$rocketName"
}

fun NavGraphBuilder.rocketDetailScreen(
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    composable(
        route = RocketDetailScreen.route,
        arguments = RocketDetailScreen.navArguments,
    ) { backStackEntry ->
        val viewModel: RocketDetailViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val rocketId = backStackEntry.arguments!!.getString(ROCKET_ID)!!
        val rocketName = backStackEntry.arguments!!.getString(ROCKET_NAME)

        LaunchedEffect(rocketId) {
            viewModel.initialize(rocketId)
        }

        RocketDetailScreen(
            uiState = uiState,
            rocketName = rocketName,
            onBackClick = onBackClick,
            onLaunchClick = onLaunchClick,
            onErrorClick = { viewModel.fetchRocket(rocketId) },
        )
    }
}

fun NavController.navigateToRocketDetail(rocketId: String, rocketName: String? = null, navOptions: NavOptions? = null) {
    this.navigate(
        route = RocketDetailScreen.createRoute(rocketId, rocketName),
        navOptions = navOptions,
    )
}

@Composable
fun RocketDetailScreen(
    uiState: UiScreenState<RocketDetailUiState>,
    rocketName: String?,
    onBackClick: () -> Unit = { },
    onLaunchClick: () -> Unit = { },
    onErrorClick: () -> Unit = { },
) {
    val rocket = (uiState as? UiScreenState.Data)?.data

    Scaffold(
        topBar = {
            DetailScreenTopBar(
                // TODO, because we does not persist data, use a placeholder for title
                //  when not loaded yet nor provided via argument.
                title = rocket?.name ?: rocketName ?: stringResource(R.string.loading),
                onBackClick = onBackClick,
                onLaunchClick = onLaunchClick,
            )
        },
    ) { innerPadding ->
        StateFullPullToRefresh(
            uiState = uiState,
            modifier =
            Modifier
                .fillMaxSize()
                .background(RocketAppTheme.colors.primaryContainer)
                .padding(innerPadding),
            onRefresh = onErrorClick,
        ) {
            RocketDetailContent(it)
        }
    }
}

@Composable
private fun DetailScreenTopBar(
    title: String,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    TopBarWithBackIcon(
        title = title,
        onBackClick = onBackClick,
        actions = {
            Text(
                text = stringResource(R.string.launch),
                modifier = Modifier
                    .clickable { onLaunchClick() }
                    .padding(RocketAppTheme.dimens.defaultPadding),
                style = RocketAppTheme.typography.topBar.copy(
                    color = RocketAppTheme.colors.actionItem,
                ),
            )
        },
    )
}

@Composable
private fun RocketDetailContent(rocket: RocketDetailUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(RocketAppTheme.dimens.largePadding),
    ) {
        Overview(rocket.overview)

        Parameters(rocket)

        ContentSpaceVertical()

        StageCard(stage = rocket.firstStage)

        ContentSpaceVertical()

        StageCard(stage = rocket.secondStage)

        ContentSpaceVertical()

        Photos(rocket.flickrImages)
    }
}

@Composable
private fun ContentSpaceVertical() {
    Spacer(modifier = Modifier.height(RocketAppTheme.dimens.defaultSpacerSize))
}

@Composable
private fun Overview(overview: String) {
    Title(stringResource(id = R.string.rocket_detail_overview))
    Text(
        text = overview,
        style = RocketAppTheme.typography.bodyLarge,
        color = RocketAppTheme.colors.onPrimaryContainer,
    )
}

@Composable
private fun Parameters(rocket: RocketDetailUiState) {
    Title(stringResource(id = R.string.rocket_detail_parameters))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(RocketAppTheme.dimens.zeroPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ParameterCard(
            valueUnit = String.format("%.0fm", rocket.heightMeters),
            quantity = stringResource(R.string.rocket_detail_card_height),
        )
        ParameterCard(
            valueUnit = String.format("%.0fm", rocket.diameterMeters),
            quantity = stringResource(R.string.rocket_detail_card_diameter),
        )
        ParameterCard(
            valueUnit = String.format("%.0ft", rocket.massTons),
            quantity = stringResource(R.string.rocket_detail_card_mass),
        )
    }
}

@Composable
private fun ParameterCard(valueUnit: String, quantity: String) {
    Card(
        modifier = Modifier
            .size(RocketAppTheme.dimens.parameterCardSize),
        shape = RoundedCornerShape(RocketAppTheme.dimens.defaultCornerSize),
        colors = CardDefaults.cardColors(
            containerColor = RocketAppTheme.colors.primary,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = valueUnit,
                    style = RocketAppTheme.typography.cardParameterValue,
                    color = RocketAppTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = quantity,
                    // TODO For such a small area texts, here will be trouble with translations.
                    //  Can be solved by dynamic text size?
                    style = RocketAppTheme.typography.cardParameter,
                    color = RocketAppTheme.colors.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun StageCard(stage: StageUiState) {
    StageCard(
        title = stage.title.asString(),
        reusable = stage.reusable.asString(),
        engines = stage.engines.asString(),
        fuel = stage.fuelAmount.asString(),
        burn = stage.burnTimeSec.asString(),
    )
}

@Composable
private fun StageCard(
    title: String,
    reusable: String,
    engines: String,
    fuel: String,
    burn: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(RocketAppTheme.dimens.defaultCornerSize),
        colors = CardDefaults.cardColors(
            containerColor = RocketAppTheme.colors.secondaryContainer,
            // TODO content color does not work as expected.
            contentColor = RocketAppTheme.colors.onSecondaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RocketAppTheme.dimens.extraLargePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StageTitle(title)
            Spacer(modifier = Modifier.height(RocketAppTheme.dimens.smallSpacerSize))
            TextWithIcon(reusable, R.drawable.reusable, R.string.reusable_icon_content_description)
            TextWithIcon(engines, R.drawable.engine, R.string.engine_icon_content_description)
            TextWithIcon(fuel, R.drawable.fuel, R.string.fuel_icon_content_description)
            TextWithIcon(burn, R.drawable.burn, R.string.burn_icon_content_description)
        }
    }
}

@Composable
private fun StageTitle(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = text,
        style = RocketAppTheme.typography.titleLarge,
        color = RocketAppTheme.colors.onSecondaryContainer,
    )
}

@Composable
private fun TextWithIcon(
    text: String,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int = R.string.rocket_icon_content_description,
) {
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(RocketAppTheme.dimens.stageItemIconSize)
                .padding(RocketAppTheme.dimens.defaultPadding),
            painter = painterResource(id = icon),
            tint = RocketAppTheme.colors.primary,
            contentDescription = stringResource(contentDescription),
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterStart),
            text = text,
            style = RocketAppTheme.typography.bodyLarge,
            color = RocketAppTheme.colors.onSecondaryContainer,
        )
    }
}

@Composable
private fun Photos(flickrImages: List<String>) {
    Title(stringResource(id = R.string.rocket_detail_photos))

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        flickrImages.forEach {
            RocketImageCard(it)
        }
    }
}

@Composable
private fun RocketImageCard(url: String) {
    AsyncImage(
        modifier = Modifier
            .padding(vertical = RocketAppTheme.dimens.defaultPadding)
            .fillMaxWidth()
            .clip(RoundedCornerShape(RocketAppTheme.dimens.defaultCornerSize)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.rocket_idle),
        error = painterResource(R.drawable.rocket_error),
        contentDescription = stringResource(R.string.rocket_icon_content_description),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Title(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = RocketAppTheme.dimens.defaultPadding),
        text = text,
        style = RocketAppTheme.typography.titleLarge,
        color = RocketAppTheme.colors.onPrimaryContainer,
    )
}

@PreviewRocketApp
@Composable
private fun RocketDetailScreenContentPreview() {
    RocketAppTheme {
        val rocket = previewRocketDetail()
        RocketDetailScreen(UiScreenState.Data(rocket), rocket.name)
    }
}

private fun previewRocketDetail(): RocketDetailUiState {
    return RocketDetailUiState(
        id = "1",
        name = "Falcon 1",
        overview = "Falcon 1 was an expendable launch system privately developed and manufactured by SpaceX during 2006-2009. On 28 September 2008, Falcon 1 became the first privately-developed liquid-fuel launch vehicle to go into orbit around the Earth.",
        heightMeters = 22.25,
        diameterMeters = 1.68,
        massTons = 30.0,
        firstStage = previewStageUiState(
            title = R.string.rocket_detail_first_stage,
            reusable = false,
            engines = 1,
            fuelAmountTons = 44.3,
            burnTimeSec = 169,
        ),
        secondStage = previewStageUiState(
            title = R.string.rocket_detail_second_stage,
            reusable = false,
            engines = 1,
            fuelAmountTons = 3.38,
            burnTimeSec = 378,
        ),
        flickrImages = listOf(
            "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg",
            "https://farm4.staticflickr.com/3955/32915197674_eee74d81bb_b.jpg",
            "https://farm1.staticflickr.com/293/32312415025_6841e30bf1_b.jpg",
            "https://farm1.staticflickr.com/623/23660653516_5b6cb301d1_b.jpg",
            "https://farm6.staticflickr.com/5518/31579784413_d853331601_b.jpg",
        ),
    )
}
