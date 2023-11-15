@file:OptIn(ExperimentalMaterialApi::class)

package cz.quanti.razym.rocketapp.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cz.quanti.razym.rocketapp.presentation.ShowMessageInSnackBar
import cz.quanti.razym.rocketapp.presentation.UiScreenState
import cz.quanti.razym.rocketapp.presentation.UiText
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme

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
fun <T> StateFullPullToRefresh(
    uiState: UiScreenState<T>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = { },
    successContent: @Composable (T) -> Unit = { },
) {
    // TODO check refreshing like this, or use a separate field in UiScreenState? (in all states)
    val refreshing =
        uiState is UiScreenState.Loading ||
            (uiState is UiScreenState.Data<*> && uiState.refreshing)

    PullToRefresh(refreshing = refreshing, onRefresh = onRefresh, modifier = modifier) {
        when (uiState) {
            is UiScreenState.Error -> {
                ContentStatusText(
                    text = uiState.errorMessage,
                    onClick = onRefresh,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                )
            }
            is UiScreenState.Loading -> {
                ContentStatusText(
                    text = uiState.message,
                )
            }
            is UiScreenState.Data -> {
                if (uiState.errorMessage != UiText.Empty) {
                    uiState.ShowMessageInSnackBar()
                }
                successContent(uiState.data)
            }
        }
    }
}

@RocketAppPreview
@Composable
private fun PullToRefreshPreview() {
    RocketappTheme {
        PullToRefresh(
            refreshing = false,
            modifier = Modifier
                .background(RocketappTheme.colors.background),
            onRefresh = {},
        ) {
            ContentStatusText(text = UiText.DynamicString("Content"))
        }
    }
}

@RocketAppPreview
@Composable
private fun StateFullPullToRefreshPreview(
    @PreviewParameter(StateProvider::class) uiState: UiScreenState<String>,
) {
    RocketappTheme {
        StateFullPullToRefresh(
            uiState = uiState,
            modifier = Modifier
                .background(RocketappTheme.colors.background),
            onRefresh = {},
        ) {
            ContentStatusText(text = UiText.DynamicString("Content"))
        }
    }
}

private class StateProvider : PreviewParameterProvider<UiScreenState<String>> {
    override val values =
        sequenceOf(
            UiScreenState.Data(
                data = "Content",
                refreshing = false,
            ),
            UiScreenState.Data(
                data = "Content",
                refreshing = true,
            ),
            UiScreenState.Loading(
                message = UiText.DynamicString("Loading"),
            ),
            UiScreenState.Error(
                errorMessage = UiText.DynamicString("Error"),
            ),
        )
}
