package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.asRocketDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RocketDetailViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

    data class ScreenUiState(
        var loading: Boolean = false,
        var rocket: RocketDetailUiState? = null,
        val messages: MutableList<String> = mutableListOf(),
    )

    private val _uiState = MutableStateFlow(ScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchRocket(id: String) {
        viewModelScope.launch {
            repo.getRocket(id)
                .asResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> _uiState.update {
                            it.copy(
                                loading = true,
                            )
                        }

                        is Result.Success -> _uiState.update {
                            it.copy(
                                loading = false,
                                rocket = result.data.asRocketDetail().asRocketDetailUiState(),
                            )
                        }

                        is Result.Error -> _uiState.update {
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
