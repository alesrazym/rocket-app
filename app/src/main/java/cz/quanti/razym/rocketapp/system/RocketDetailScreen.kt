@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.razym.rocketapp.system

import android.content.res.Configuration
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.model.Stage
import cz.quanti.razym.rocketapp.presentation.RocketDetailUiState
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.razym.rocketapp.presentation.StageUiState
import cz.quanti.razym.rocketapp.presentation.UiScreenState
import cz.quanti.razym.rocketapp.presentation.asStageUiState
import cz.quanti.razym.rocketapp.ui.StateFullPullToRefresh
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.androidx.compose.getViewModel

private const val ROCKET_DETAIL = "rocketDetail"
private const val ROCKET_ID = "rocketId"
private const val ROCKET_NAME = "rocketName"

data object RocketDetailScreen : Screen(
    route = "$ROCKET_DETAIL/{$ROCKET_ID}?rocketName={$ROCKET_NAME}",
    navArguments = listOf(
        navArgument(ROCKET_ID) { type = NavType.StringType },
        navArgument(ROCKET_NAME) { type = NavType.StringType; nullable = true },
    )
) {
    fun createRoute(rocketId: String, rocketName: String? = null) =
        "$ROCKET_DETAIL/${rocketId}?rocketName=${rocketName}"
}

fun NavGraphBuilder.rocketDetailScreen(
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    composable(
        route = RocketDetailScreen.route,
        arguments = RocketDetailScreen.navArguments,
    ) { backStackEntry ->
        val viewModel: RocketDetailViewModel = getViewModel()
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
    val rocket = (uiState as? UiScreenState.Success)?.data

    Scaffold(
        topBar = {
            TopBar(
                // TODO, because we does not persist data, use a placeholder for title
                //  when not loaded yet nor provided via argument.
                title = rocket?.name ?: rocketName ?: stringResource(R.string.loading),
                onBackClick = onBackClick,
                onLaunchClick = onLaunchClick,
            )
        },
        contentColor = RocketappTheme.colors.onBackground,
    ) { innerPadding ->
        StateFullPullToRefresh(
            uiState = uiState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(RocketappTheme.colors.primaryContainer)
                    .padding(innerPadding),
            onRefresh = onErrorClick,
        ) {
            RocketDetailContent(rocket!!)
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = RocketappTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
                color = RocketappTheme.colors.onBackground,
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(RocketappTheme.dimens.navigationIconSize)
                    .clickable { onBackClick() }
                    .padding(RocketappTheme.dimens.defaultPadding)
                ,
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_to_list_back_arrow),
            )
        },
        actions = {
            Text(
                text = stringResource(R.string.launch),
                modifier = Modifier
                    .clickable { onLaunchClick() }
                    .padding(RocketappTheme.dimens.defaultPadding)
                ,
                style = RocketappTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = RocketappTheme.colors.actionItem,
                ),
            )
        },
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
private fun RocketDetailContent(rocket: RocketDetailUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(RocketappTheme.dimens.largePadding)
    ) {
        Overview(rocket.overview)

        Parameters(rocket)

        ContentSpaceVertical()

        StageCard(
            stage = rocket.firstStage,
            title = stringResource(R.string.rocket_detail_first_stage),
        )

        ContentSpaceVertical()

        StageCard(
            stage = rocket.secondStage,
            title = stringResource(R.string.rocket_detail_second_stage),
        )

        ContentSpaceVertical()

        Photos(rocket.flickrImages)
    }
}

@Composable
private fun ContentSpaceVertical() {
    Spacer(modifier = Modifier.height(RocketappTheme.dimens.defaultSpacerSize))
}

@Composable
private fun Overview(overview: String) {
    Title(stringResource(id = R.string.rocket_detail_overview))
    Text(
        text = overview,
        style = RocketappTheme.typography.bodyLarge,
        color = RocketappTheme.colors.onPrimaryContainer,
    )
}

@Composable
private fun Parameters(rocket: RocketDetailUiState) {
    Title(stringResource(id = R.string.rocket_detail_parameters))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(RocketappTheme.dimens.zeroPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ParameterCard(
            valueUnit = String.format("%.0fm", rocket.heightMeters),
            quantity = stringResource(R.string.rocket_detail_card_height),
        )
        ParameterCard(
            valueUnit = String.format("%.0fm", rocket.diameterMeters),
            quantity = stringResource(R.string.rocket_detail_card_diameter)
        )
        ParameterCard(
            valueUnit = String.format("%.0ft", rocket.massTons),
            quantity = stringResource(R.string.rocket_detail_card_mass)
        )
    }
}

@Composable
private fun ParameterCard(valueUnit: String, quantity: String) {
    Card(
        modifier = Modifier
            .size(RocketappTheme.dimens.parameterCardSize),
        shape = RoundedCornerShape(RocketappTheme.dimens.defaultCornerSize),
        colors = CardDefaults.cardColors(
            containerColor = RocketappTheme.colors.primary,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = valueUnit,
                    style = RocketappTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = RocketappTheme.colors.onPrimary,
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
                    // TODO big step between medium 16sp and large 22sp.
                    //  Also, for such a small area texts, here will be trouble with translations.
                    //  Can be solved by dynamic text size?
                    style = RocketappTheme.typography.titleLarge,
                    color = RocketappTheme.colors.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun StageCard(stage: StageUiState, title: String = "") {
    StageCard(
        title = title,
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
    burn: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(RocketappTheme.dimens.defaultCornerSize),
        colors = CardDefaults.cardColors(
            containerColor = RocketappTheme.colors.secondaryContainer,
            // TODO content color does not work as expected.
            contentColor = RocketappTheme.colors.onSecondaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RocketappTheme.dimens.extraLargePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StageTitle(title)
            Spacer(modifier = Modifier.height(RocketappTheme.dimens.smallSpacerSize))
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
            .fillMaxWidth()
        ,
        text = text,
        style = RocketappTheme.typography.titleLarge,
        color = RocketappTheme.colors.onSecondaryContainer,
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
                .size(RocketappTheme.dimens.stageItemIconSize)
                .padding(RocketappTheme.dimens.defaultPadding)
            ,
            painter = painterResource(id = icon),
            tint = RocketappTheme.colors.primary,
            contentDescription = stringResource(contentDescription),
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterStart),
            text = text,
            style = RocketappTheme.typography.bodyLarge,
            color = RocketappTheme.colors.onSecondaryContainer,
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
            .padding(vertical = RocketappTheme.dimens.defaultPadding)
            .fillMaxWidth()
            .clip(RoundedCornerShape(RocketappTheme.dimens.defaultCornerSize)),
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
            .padding(vertical = RocketappTheme.dimens.defaultPadding),
        text = text,
        style = RocketappTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        color = RocketappTheme.colors.onPrimaryContainer,
    )
}

@Preview(
    showBackground = true,
    widthDp = PREVIEW_WIDTH,
    heightDp = 600,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark",
)
@Preview(showBackground = true, widthDp = PREVIEW_WIDTH, heightDp = 600)
@Composable
fun RocketDetailScreenContentPreview() {
    RocketappTheme {
        val rocket = previewRocketDetail()
        RocketDetailScreen(UiScreenState.Success(rocket), rocket.name)
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
        firstStage = Stage(
            reusable = false,
            engines = 1,
            fuelAmountTons = 44.3,
            burnTimeSec = 169
        ).asStageUiState(),
        secondStage = Stage(
            reusable = false,
            engines = 1,
            fuelAmountTons = 3.38,
            burnTimeSec = 378
        ).asStageUiState(),
        flickrImages = listOf(
            "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg",
            "https://farm4.staticflickr.com/3955/32915197674_eee74d81bb_b.jpg",
            "https://farm1.staticflickr.com/293/32312415025_6841e30bf1_b.jpg",
            "https://farm1.staticflickr.com/623/23660653516_5b6cb301d1_b.jpg",
            "https://farm6.staticflickr.com/5518/31579784413_d853331601_b.jpg"
        )
    )
}
