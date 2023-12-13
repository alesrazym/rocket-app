package cz.quanti.razym.rocketapp.presentation

import android.util.MalformedJsonException
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonDataException
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.asResult
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.asRocketDetail
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
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
                    loadingMessage = UiText.StringResource(R.string.getting_rocket_detail_in_progress)
                )
        }
    }
}
