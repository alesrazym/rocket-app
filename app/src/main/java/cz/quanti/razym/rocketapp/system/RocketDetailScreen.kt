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
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.presentation.RocketDetail
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.razym.rocketapp.presentation.Stage
import cz.quanti.razym.rocketapp.ui.theme.LocalColors
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.androidx.compose.getViewModel


private const val ROCKET_DETAIL = "rocketDetail"

private const val ROCKET_ID = "rocketId"

data object RocketDetailScreen : Screen(
    route = "$ROCKET_DETAIL/{$ROCKET_ID}",
    navArguments = listOf(
        navArgument(ROCKET_ID) { type = NavType.StringType },
    )
) {
    fun createRoute(rocketId: String) = "$ROCKET_DETAIL/${rocketId}"
}

fun NavGraphBuilder.rocketDetailScreen(
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    composable(
        route = RocketDetailScreen.route,
        arguments = RocketDetailScreen.navArguments,
    ) {
        val viewModel: RocketDetailViewModel = getViewModel()
        val uiState by viewModel.uiState.collectAsState()

        val rocketId = it.arguments!!.getString(RocketDetailScreen.navArguments[0].name)!!

        LaunchedEffect(rocketId) {
            // TODO this is not perfect condition, as after configuration change
            //  it will reload data if there has been an error and there are no rockets then.
            if (!uiState.loading && uiState.rocket == null) {
                viewModel.fetchRocket(rocketId)
            }
        }

        RocketDetailScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onLaunchClick = onLaunchClick,
            onErrorClick = { viewModel.fetchRocket(rocketId) },
        )
    }
}

fun NavController.navigateToRocketDetail(rocketId: String, navOptions: NavOptions? = null) {
    this.navigate(
        route = RocketDetailScreen.createRoute(rocketId),
        navOptions = navOptions
    )
}

@Composable
fun RocketDetailScreen(
    uiState: RocketDetailViewModel.ScreenUiState,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
    onErrorClick: () -> Unit = {},
) {
    val rocket = uiState.rocket
    val loading = uiState.loading

    RocketDetailScreen(loading, rocket, onBackClick, onLaunchClick, onErrorClick)
}

@Composable
private fun RocketDetailScreen(
    loading: Boolean,
    rocket: RocketDetail?,
    onBackClick: () -> Unit = { },
    onLaunchClick: () -> Unit = { },
    onErrorClick: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopBar(
                // TODO, because we does not persist data, use a placeholder for title
                //  when not loaded yet.
                title = rocket?.name ?: "Loading",
                onBackClick = onBackClick,
                onLaunchClick = onLaunchClick,
            )
        },
        contentColor = LocalColors.current.onBackground,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColors.current.primaryContainer)
                .padding(innerPadding)
            ,
        ) {
            if (rocket == null && loading) {
                ContentStatusText(
                    text = R.string.getting_rocket_detail_in_progress,
                )
            } else {
                if (rocket == null) {
                    ContentStatusText(
                        text = R.string.getting_rocket_detail_failed,
                        onClick = onErrorClick,
                    )
                } else {
                    RocketDetailContent(rocket)
                }
            }

            // TODO handle error messages

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
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
                color = LocalColors.current.onBackground,
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onBackClick() }
                    .padding(8.dp)
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
                    .padding(8.dp)
                ,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.current.actionItem,
                ),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
                containerColor = LocalColors.current.background,
                scrolledContainerColor = LocalColors.current.background,
                navigationIconContentColor = LocalColors.current.onBackground,
                // TODO why does navigation content color works and title and action does not?
                titleContentColor = LocalColors.current.onBackground,
                actionIconContentColor = LocalColors.current.actionItem,
        ),
    )
}

@Composable
private fun RocketDetailContent(rocket: RocketDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Overview(rocket.overview)

        Parameters(rocket)

        Spacer(modifier = Modifier.height(16.dp))

        StageCard(
            stage = rocket.firstStage,
            title = stringResource(R.string.rocket_detail_first_stage),
        )

        Spacer(modifier = Modifier.height(16.dp))

        StageCard(
            stage = rocket.secondStage,
            title = stringResource(R.string.rocket_detail_second_stage),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Photos(rocket.flickrImages)
    }
}

@Composable
private fun Overview(overview: String) {
    Title(stringResource(id = R.string.rocket_detail_overview))
    Text(
        text = overview,
        style = MaterialTheme.typography.bodyLarge,
        color = LocalColors.current.onPrimaryContainer,
    )
}

@Composable
private fun Parameters(rocket: RocketDetail) {
    Title(stringResource(id = R.string.rocket_detail_parameters))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
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
            .size(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.current.primary,
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
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = LocalColors.current.onPrimary,
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
                    style = MaterialTheme.typography.titleLarge,
                    color = LocalColors.current.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun StageCard(stage: Stage, title: String = "") {
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.current.secondaryContainer,
            // TODO content color does not work as expected.
            contentColor = LocalColors.current.onSecondaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StageTitle(title)
            Spacer(modifier = Modifier.height(8.dp))
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
        style = MaterialTheme.typography.titleLarge,
        color = LocalColors.current.onSecondaryContainer,
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
                .size(40.dp)
                .padding(8.dp)
            ,
            painter = painterResource(id = icon),
            tint = LocalColors.current.primary,
            contentDescription = stringResource(contentDescription),
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterStart),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = LocalColors.current.onSecondaryContainer,
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
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.rocket_idle),
        error = painterResource(R.drawable.rocket_error),
        contentDescription = stringResource(R.string.rocket_icon_content_description),
    )
}

@Composable
private fun Title(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        color = LocalColors.current.onPrimaryContainer,
    )
}

@Preview(
    showBackground = true,
    widthDp = previewWidth,
    heightDp = 600,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark",
)
@Preview(showBackground = true, widthDp = previewWidth, heightDp = 600)
@Composable
fun RocketDetailScreenContentPreview() {
    RocketappTheme {
        RocketDetailScreen(false, previewRocketDetail())
    }
}

private fun previewRocketDetail(): RocketDetail {
    return RocketDetail(
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
        ),
        secondStage = Stage(
            reusable = false,
            engines = 1,
            fuelAmountTons = 3.38,
            burnTimeSec = 378
        ),
        flickrImages = listOf(
            "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg",
            "https://farm4.staticflickr.com/3955/32915197674_eee74d81bb_b.jpg",
            "https://farm1.staticflickr.com/293/32312415025_6841e30bf1_b.jpg",
            "https://farm1.staticflickr.com/623/23660653516_5b6cb301d1_b.jpg",
            "https://farm6.staticflickr.com/5518/31579784413_d853331601_b.jpg"
        )
    )
}
