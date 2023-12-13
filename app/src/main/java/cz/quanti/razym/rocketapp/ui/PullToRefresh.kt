@file:OptIn(ExperimentalMaterialApi::class)

package cz.quanti.razym.rocketapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.quanti.razym.rocketapp.presentation.UiScreenState

@Composable
fun PullToRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = rememberPullRefreshState(refreshing, onRefresh)

    Box(
        modifier
            .pullRefresh(state)
            .fillMaxSize()
    ) {
        content()

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
fun StateFullPullToRefresh(
    uiState: UiScreenState<*>,
    onRefresh: () -> Unit,
    successContent: @Composable () -> Unit = { },
) {
    // TODO check refreshing like this, or use a separate field in UiScreenState? (in all states)
    val refreshing = uiState is UiScreenState.Loading ||
        (uiState is UiScreenState.Success<*> && uiState.refreshing)

    PullToRefresh(refreshing = refreshing, onRefresh = onRefresh) {
        when (uiState) {
            is UiScreenState.Error ->
                ContentStatusText(
                    text = uiState.errorMessage,
                    onClick = onRefresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                )
            is UiScreenState.Loading ->
                ContentStatusText(
                    text = uiState.message,
                )
            is UiScreenState.Success ->
                successContent()
        }

        // TODO handle error messages

    }
}
