package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result.*
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import kotlinx.coroutines.launch
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

// TODO improve error state handling
// TODO interface needed?
sealed interface UiState {
    data class Success(val rockets: List<Rocket>) : UiState
    object Error : UiState
    object Loading : UiState
}

data class ScreenUiState(
    val state: UiState
)

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

    // TODO configure state flow, eg as follows? Or uiState below is good enough?

    val rockets: Flow<List<Rocket>> = repo.getRockets().stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val _uiState = MutableStateFlow(ScreenUiState(UiState.Loading))
    val uiState = _uiState.asStateFlow()

    init {
        fetchRockets()
    }

    fun fetchRockets() {
        viewModelScope.launch {

            // TODO where to convert from data layer to presentation layer?
            // TODO add extension function to convert rocket data to rocket model
            // TODO add unit tests for extension function
            // TODO add unit tests for view model
            // TODO add unit test for data to model conversion

            repo.getRockets().asResult()
                .collect { result ->
                    val genreUiState = when (result) {
                        is Success -> UiState.Success(result.data)
                        is Loading -> UiState.Loading
                        is Error -> UiState.Error
                    }

                    _uiState.value = ScreenUiState(genreUiState)
                }
        }
    }
}
