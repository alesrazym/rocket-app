package cz.quanti.razym.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result.Error
import cz.quanti.razym.rocketapp.Result.Loading
import cz.quanti.razym.rocketapp.Result.Success
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.data.parseFirstFlight
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

    data class ScreenUiState(
        var loading: Boolean = false,
        // Null for not loaded yet, empty list for no rockets.
        var rockets: List<Rocket>? = null,
        val messages: MutableList<String> = mutableListOf(),
    )

    private var initializeCalled = false
    private val _uiState = MutableStateFlow(ScreenUiState())
    val uiState = _uiState.asStateFlow()

    @MainThread
    fun initialize() {
        if(initializeCalled)
            return

        initializeCalled = true

        fetchRockets()
    }

    fun fetchRockets() {
        viewModelScope.launch {
            repo.getRockets()
                .asResult()
                .collect { result ->
                    when (result) {
                        is Loading -> _uiState.update {
                            it.copy(
                                loading = true,
                            )
                        }

                        is Success -> _uiState.update {
                            it.copy(
                                loading = false,
                                rockets = result.data.map { data ->
                                    data.asRocket()
                                },
                            )
                        }

                        is Error -> _uiState.update {
                            it.copy(
                                loading = false,
                                messages = it.messages.apply {
                                    add(result.exception?.message ?: "Unknown error")
                                },
                            )
                        }
                    }
                }
        }
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(
        this.name,
        this.parseFirstFlight(),
        this.id)
}
