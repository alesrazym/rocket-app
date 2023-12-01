package cz.quanti.razym.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketropository.data.RocketData
import cz.quanti.razym.rocketropository.domain.RocketsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate
import java.util.Date

class RocketListViewModel(
    private val repo: RocketsRepository,
) : ViewModel() {
    private var initializeCalled = false
    private val _uiState: MutableStateFlow<UiScreenState<List<Rocket>>> =
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
            repo.getRockets()
                .asResult()
                .update(
                    uiState = _uiState,
                    transform = { data -> data.map { it.asRocket() } },
                    loadingMessage = UiText.StringResource(R.string.rockets_loading)
                )
        }
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(
        this.name,
        // TODO temporary java Date, maybe incorrect zone convert...
        Date(this.firstFlight.toLocalDate().atStartOfDayIn(
            TimeZone.UTC).toEpochMilliseconds()),
        this.id,
    )
}
