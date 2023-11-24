@file:OptIn(ExperimentalMaterial3Api::class)

package cz.quanti.razym.rocketapp.system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.quanti.razym.rocketapp.R

@Preview
@Composable
fun RocketLaunchScreen(
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            RocketLaunchFragmentTopBar(
                title = stringResource(R.string.launch),
                onBackClick = onBackClick,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Rocket Launch",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun RocketLaunchFragmentTopBar(
    title: String,
    onBackClick: () -> Unit = {},
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
    )
}
