package cz.quanti.rocketapp.android.feature.rocket.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiScreenState
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiText
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketDetailViewModel(
    private val getRocketUseCase: GetRocketUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiScreenState<RocketDetailUiState>> =
        MutableStateFlow(UiScreenState.Loading(UiText.StringResource(R.string.getting_rocket_detail_in_progress)))
    val uiState = _uiState.asStateFlow()

    private var initializeCalled = false

    @MainThread
    fun initialize(id: String) {
        if (initializeCalled) {
            return
        }

        initializeCalled = true

        fetchRocket(id)
    }

    fun fetchRocket(id: String) {
        viewModelScope.launch {
            _uiState
                .update(
                    resultFlow = getRocketUseCase(id),
                    transform = { data -> data.asRocketDetailUiState() },
                    loadingMessage = UiText.StringResource(R.string.getting_rocket_detail_in_progress),
                )
        }
    }
}
