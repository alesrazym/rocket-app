package cz.quanti.rocketapp.android.feature.rocket.presentation

import cz.quanti.rocketapp.android.feature.rocket.util.toDate
import cz.quanti.rocketapp.android.feature.rocket.util.toLocalString
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiText
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Rocket
import java.util.Date

data class RocketUiState(
    val name: UiText,
    val firstFlight: UiText,
    val id: String,
)

fun Rocket.asRocketUiState(): RocketUiState {
    return RocketUiState(
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
