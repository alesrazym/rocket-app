package cz.quanti.razym.rocketapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.util.toDate
import cz.quanti.razym.rocketapp.util.toLocalString
import cz.quanti.razym.rocketropository.data.RocketData
import cz.quanti.razym.rocketropository.domain.GetRocketsUseCase
import cz.quanti.razym.rocketropository.domain.invoke
import java.util.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RocketListViewModel(
    private val getRocketsUseCase: GetRocketsUseCase,
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
            getRocketsUseCase()
                .asResult()
                .update(
                    uiState = _uiState,
                    transform = { data -> data.map { it.asRocket() } },
                    loadingMessage = UiText.StringResource(R.string.rockets_loading),
                )
        }
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(
        UiText.DynamicString(name),
        firstFlight.toDate().asFirstFlightUiText(),
        id,
    )
}

fun Date?.asFirstFlightUiText(): UiText {
    return if (this == null) {
        UiText.StringResource(
            R.string.first_flight,
            UiText.StringResource(R.string.first_flight_unknown),
        )
    } else {
        UiText.StringResource(
            R.string.first_flight,
            this.toLocalString(),
        )
    }
}

