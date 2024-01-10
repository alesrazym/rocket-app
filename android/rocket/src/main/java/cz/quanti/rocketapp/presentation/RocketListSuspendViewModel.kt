package cz.quanti.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketropository.domain.GetRocketsSuspendUseCase
import cz.quanti.rocketropository.domain.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketListSuspendViewModel(
    private val getRocketsUseCase: GetRocketsSuspendUseCase,
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
            // As suspend function cannot emit multiple values, set loading state manually.
            // `loadingMessage` in `update` function is not used, result can only be success or error.
            _uiState.loading(UiText.StringResource(R.string.rockets_loading))
            _uiState
                .update(
                    result = getRocketsUseCase(),
                    transform = { data -> data.map { it.asRocketUiState() } },
                )
        }
    }
}
