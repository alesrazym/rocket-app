package cz.quanti.razym.rocketapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.Result
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.data.StageData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.RocketDetail
import cz.quanti.razym.rocketapp.model.Stage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketDetailViewModel(
    private val repo: RocketsRepository,
) : ViewModel() {
    // TODO improve error state handling
    // TODO interface needed?
    sealed interface UiState {
        data class Success(val rocket: RocketDetail) : UiState

        data object Error : UiState

        data object Loading : UiState
    }

    data class ScreenUiState(
        val state: UiState,
    )

    private val _uiState = MutableStateFlow(ScreenUiState(UiState.Loading))
    val uiState = _uiState.asStateFlow()

    fun fetchRocket(id: String) {
        viewModelScope.launch {
            repo.getRocket(id)
                .asResult()
                .collect { result ->
                    val state =
                        when (result) {
                            is Result.Success -> UiState.Success(result.data.asRocketDetail())
                            is Result.Loading -> UiState.Loading
                            is Result.Error -> UiState.Error
                        }

                    _uiState.value =
                        ScreenUiState(state)
                }
        }
    }
}

fun RocketData.asRocketDetail(): RocketDetail {
    return RocketDetail(
        this.name,
        this.id,
        this.overview,
        this.height[RocketData.METERS] ?: 0.0,
        this.diameter[RocketData.METERS] ?: 0.0,
        (this.mass[RocketData.KG] ?: 0.0) / 1000.0,
        this.firstStage.asStage(),
        this.secondStage.asStage(),
        this.flickrImages,
    )
}

fun StageData.asStage(): Stage {
    return Stage(this.burnTimeSec, this.engines, this.fuelAmountTons, this.reusable)
}
