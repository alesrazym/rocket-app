@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.razym.rocketapp.system

import androidx.annotation.DrawableRes
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.model.RocketDetail
import cz.quanti.razym.rocketapp.model.Stage
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun RocketDetailScreen(
    viewModel: RocketDetailViewModel = getViewModel(),
    rocketId: String,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (!uiState.loading && uiState.rocket == null)
            viewModel.fetchRocket(rocketId)
    }

    RocketDetailScreen(
        rocket = uiState.rocket,
        loading = uiState.loading,
        onBackClick = onBackClick,
        onLaunchClick = onLaunchClick,
        onErrorClick = { viewModel.fetchRocket(rocketId) },
    )
}

@Composable
private fun RocketDetailScreen(
    rocket: RocketDetail?,
    loading: Boolean,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
    onErrorClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            RocketDetailFragmentTopBar(
                // TODO, because we does not persist data, use a placeholder for title
                //  when not loaded yet.
                title = rocket?.name ?: "Loading",
                onBackClick = onBackClick,
                onLaunchClick = onLaunchClick,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (rocket == null && loading) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.getting_rocket_detail_in_progress),
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                if (rocket == null)
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable { onErrorClick() },
                        text = stringResource(R.string.getting_rocket_detail_failed),
                        style = MaterialTheme.typography.titleMedium,
                    )
                else
                    RocketDetailScreen(rocket)
            }

            // TODO handle error messages

        }
    }
}

@Composable
private fun RocketDetailFragmentTopBar(
    title: String,
    onBackClick: () -> Unit = {},
    onLaunchClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .padding(8.dp)
                    .clickable { onBackClick() },
                painter = painterResource(id = R.drawable.ic_arrow_back),
                // TODO
                contentDescription = "",
            )
        },
        actions = {
            Text(
                text = "Launch",
                Modifier.clickable { onLaunchClick() },
            )
        },
    )
}

@Composable
private fun RocketDetailScreen(rocket: RocketDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Title(stringResource(id = R.string.rocket_detail_overview))
        Text(
            text = rocket.overview,
            style = MaterialTheme.typography.bodyMedium
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        val stage1 = rocket.firstStage
        StageCard(
            title = stringResource(R.string.rocket_detail_first_stage),
            reusable = formatReusable(stage1.reusable),
            engines = pluralStringResource(
                id = R.plurals.engines,
                count = stage1.engines,
                stage1.engines
            ),
            fuel = stringResource(R.string.tons_of_fuel, stage1.fuelAmountTons),
            burn = formatBurn(stage1.burnTimeSec)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val stage2 = rocket.secondStage
        StageCard(
            title = stringResource(R.string.rocket_detail_second_stage),
            reusable = formatReusable(stage2.reusable),
            engines = pluralStringResource(
                id = R.plurals.engines,
                count = stage2.engines,
                stage2.engines
            ),
            fuel = stringResource(R.string.tons_of_fuel, stage2.fuelAmountTons),
            burn = formatBurn(stage2.burnTimeSec)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Title(stringResource(id = R.string.rocket_detail_photos))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            rocket.flickrImages.take(10).forEach {
                RocketImageCard(it)
            }
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
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun StageTitle(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        text = text,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun ParameterCard(valueUnit: String, quantity: String) {
    Card(
        modifier = Modifier
            .size(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF25187),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = valueUnit,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        }
    }
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
            containerColor = Color(0xFFF6F6F6),
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
            TextWithIcon(reusable, R.drawable.reusable)
            TextWithIcon(engines, R.drawable.engine)
            TextWithIcon(fuel, R.drawable.fuel)
            TextWithIcon(burn, R.drawable.burn)
        }
    }
}

@Composable
private fun TextWithIcon(text: String, @DrawableRes icon: Int) {
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(36.dp)
                .padding(8.dp),
            painter = painterResource(id = icon),
            // TODO
            contentDescription = "",
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterStart),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun formatBurn(burnSec: Int?): String {
    return if (burnSec == null) {
        stringResource(R.string.unknown)
    } else {
        stringResource(R.string.seconds_burn_time, burnSec)
    }
}

@Composable
fun formatReusable(reusable: Boolean): String {
    return if (reusable) {
        stringResource(R.string.reusable)
    } else {
        stringResource(R.string.not_reusable)
    }
}

@Preview
@Composable
private fun TextWithIconPreview() {
    RocketappTheme {
        TextWithIcon("Test", R.drawable.reusable)
    }
}

@Preview
@Composable
fun RocketDetailFragmentContentPreview() {
    RocketappTheme {
        RocketDetailScreen(previewRocketDetail())
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
