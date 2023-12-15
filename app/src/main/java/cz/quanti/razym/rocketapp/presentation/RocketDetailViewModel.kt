package cz.quanti.razym.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.asRocketDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketDetailViewModel(
    private val repo: RocketsRepository
)  : ViewModel() {

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
            repo.getRocket(id)
                .asResult()
                .update(
                    uiState = _uiState,
                    transform = { data -> data.asRocketDetail().asRocketDetailUiState() },
                    loadingMessage = UiText.StringResource(R.string.getting_rocket_detail_in_progress),
                )
        }
    }
}
