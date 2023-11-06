package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result.Error
import cz.quanti.razym.rocketapp.Result.Loading
import cz.quanti.razym.rocketapp.Result.Success
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

    // TODO improve error state handling
    // TODO interface needed?
    sealed interface UiState {
        data class Success(val rockets: List<Rocket>) : UiState
        data object Error : UiState
        data object Loading : UiState
    }

    data class ScreenUiState(
        val state: UiState
    )

    private val _uiState = MutableStateFlow(ScreenUiState(UiState.Loading))
    val uiState = _uiState.asStateFlow()

    init {
        fetchRockets()
    }

    fun fetchRockets() {
        viewModelScope.launch {
            repo.getRockets()
                .asResult()
                .collect { result ->
                    val state = when (result) {
                        is Success -> UiState.Success(result.data.map {
                            it.asRocket()
                        })
                        is Loading -> UiState.Loading
                        is Error -> UiState.Error
                    }

                    _uiState.value = ScreenUiState(state)
                }
        }
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(this.name, this.firstFlight, this.id)
}
