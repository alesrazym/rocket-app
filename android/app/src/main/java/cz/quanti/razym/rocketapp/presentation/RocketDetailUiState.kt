package cz.quanti.razym.rocketapp.presentation

import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.model.RocketDetail
import cz.quanti.razym.rocketapp.model.Stage

data class RocketDetailUiState(
    val name: String,
    val id: String,
    val overview: String,
    val heightMeters: Double,
    val diameterMeters: Double,
    val massTons: Double,
    val firstStage: StageUiState,
    val secondStage: StageUiState,
    val flickrImages: List<String>,
)

data class StageUiState(
    val burnTimeSec: UiText,
    val engines: UiText,
    val fuelAmount: UiText,
    val reusable: UiText,
)

fun RocketDetail.asRocketDetailUiState(): RocketDetailUiState {
    return RocketDetailUiState(
        this.name,
        this.id,
        this.overview,
        this.heightMeters,
        this.diameterMeters,
        this.massTons,
        this.firstStage.asStageUiState(),
        this.secondStage.asStageUiState(),
        this.flickrImages.take(10),
    )
}

fun Stage.asStageUiState(): StageUiState {
    return StageUiState(
        this.burnTimeSec.asBurnUiText(),
        this.engines.asEnginesUiText(),
        this.fuelAmountTons.asFuelUiText(),
        this.reusable.asReusableUiText(),
    )
}

fun Int?.asBurnUiText(): UiText {
    return if (this == null) {
        UiText.StringResource(R.string.unknown)
    } else {
        UiText.StringResource(R.string.seconds_burn_time, this)
    }
}

fun Int?.asEnginesUiText(): UiText {
    val count = this ?: 0
    return UiText.PluralResource(R.plurals.engines, count, count)
}

fun Double?.asFuelUiText(): UiText {
    return UiText.StringResource(R.string.tons_of_fuel, this ?: 0.0)
}

fun Boolean.asReusableUiText(): UiText {
    return if (this) {
        UiText.StringResource(R.string.reusable)
    } else {
        UiText.StringResource(R.string.not_reusable)
    }
}
