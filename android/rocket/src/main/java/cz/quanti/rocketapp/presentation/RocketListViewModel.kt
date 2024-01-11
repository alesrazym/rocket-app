package cz.quanti.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketropository.RocketException
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import cz.quanti.common.domain.invoke
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val getRocketsUseCase: GetRocketsUseCase,
) : ViewModel() {
    private var onlyOneJob: Job? = null
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
        if (onlyOneJob?.isActive == true) {
            return
        }

        onlyOneJob =
            viewModelScope.launch {
                _uiState
                    .update(
                        resultFlow = getRocketsUseCase(),
                        transform = { data -> data.map { it.asRocketUiState() } },
                        loadingMessage = UiText.StringResource(R.string.rockets_loading),
                    )
            }

        // TODO: remove before merge
        // Simulate user cancel.
//        viewModelScope.launch {
//            kotlinx.coroutines.delay(1000)
//            cancel()
//        }
    }

    fun cancel() {
        if (onlyOneJob?.isActive == false) {
            return
        }

        onlyOneJob?.cancel()

        // We need to propagate CancelException from flow,
        // or set state manually.
        // As job cancellation is processed synchronously,
        // we can safely set final state here.
//        _uiState.error(RocketException.CanceledByUserException("Canceled by user"))
    }
}
