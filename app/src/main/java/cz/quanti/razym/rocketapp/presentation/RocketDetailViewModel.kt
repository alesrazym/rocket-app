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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RocketDetailViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

    data class ScreenUiState(
        var loading: Boolean = false,
        var rocket: RocketDetail? = null,
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
                                rocket = result.data.asRocketDetail(),
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
        this.flickrImages)
}

fun StageData.asStage(): Stage {
    return Stage(this.burnTimeSec, this.engines, this.fuelAmountTons, this.reusable)
}
