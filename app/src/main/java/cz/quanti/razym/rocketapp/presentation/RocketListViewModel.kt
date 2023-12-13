package cz.quanti.razym.rocketapp.presentation

import android.util.MalformedJsonException
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonDataException
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.data.parseFirstFlight
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                    errorTransform = {
                        when (it) {
                            is JsonDataException -> UiText.StringResource(R.string.error_json)
                            is MalformedJsonException -> UiText.StringResource(R.string.error_json)
                            is TimeoutException -> UiText.StringResource(R.string.error_timeout)
                            is IOException -> UiText.StringResource(R.string.error_io)
                            is HttpException -> UiText.StringResource(R.string.error_server_response)
                            else -> UiText.StringResource(R.string.unknown_error)
                        }
                    },
                    loadingMessage = UiText.StringResource(R.string.rockets_loading)
                )
        }
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(
        this.name,
        this.parseFirstFlight(),
        this.id,
    )
}
