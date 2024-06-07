package cz.quanti.rocketapp.android.feature.rocket.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiScreenState
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiText
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketsUseCase
import cz.quanti.rocketapp.multiplatform.lib.common.domain.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val getRocketsUseCase: GetRocketsUseCase,
) : ViewModel() {
    private var initializeCalled = false
    private val _uiState: MutableStateFlow<UiScreenState<List<RocketUiState>>> =
        MutableStateFlow(UiScreenState.Loading(UiText.StringResource(R.string.rockets_loading)))
    val uiState = _uiState.asStateFlow()

    @MainThread
    fun initialize() {
        if (initializeCalled) {
            return
        }

        initializeCalled = true

        fetchRockets()
    }

    fun fetchRockets() {
        viewModelScope.launch {
            _uiState
                .update(
                    resultFlow = getRocketsUseCase(),
                    transform = { data -> data.map { it.asRocketUiState() } },
                    loadingMessage = UiText.StringResource(R.string.rockets_loading),
                )
        }
    }
}
