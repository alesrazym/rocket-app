package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result.Error
import cz.quanti.razym.rocketapp.Result.Loading
import cz.quanti.razym.rocketapp.Result.Success
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.GetRocketsUseCase
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private val getRocketsUseCase: GetRocketsUseCase
)  : ViewModel() {
    private val _uiState = MutableStateFlow(ScreenUiState(UiState.Loading))
    val uiState = _uiState.asStateFlow()

    init {
        fetchRockets()
    }

    fun fetchRockets() {
        viewModelScope.launch {

            getRocketsUseCase()
                .asResult()
                .collect { result ->
                    val state = when (result) {
                        is Success -> UiState.Success(result.data.map {
                            it.model()
                        })
                        is Loading -> UiState.Loading
                        is Error -> UiState.Error
                    }

                    _uiState.value = ScreenUiState(state)
                }
        }
    }
}

fun RocketData.model(): Rocket {
    return Rocket(this.name,"First flight: ${this.firstFlight}")
}