package cz.quanti.rocketapp.android.feature.rocket.presentation

import androidx.annotation.StringRes
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiText
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketDetail
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Stage

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
    val title: UiText,
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
        this.firstStage.asStageUiState(UiText.StringResource(R.string.rocket_detail_first_stage)),
        this.secondStage.asStageUiState(UiText.StringResource(R.string.rocket_detail_second_stage)),
        this.flickrImages.take(n = 10),
    )
}

fun Stage.asStageUiState(title: UiText): StageUiState {
    return StageUiState(
        title,
        this.burnTimeSec.asBurnUiText(),
        this.engines.asEnginesUiText(),
        this.fuelAmountTons.asFuelUiText(),
        this.reusable.asReusableUiText(),
    )
}

fun previewStageUiState(
    @StringRes title: Int,
    burnTimeSec: Int?,
    engines: Int,
    fuelAmountTons: Double,
    reusable: Boolean,
) = StageUiState(
    title = UiText.StringResource(title),
    burnTimeSec = burnTimeSec.asBurnUiText(),
    engines = engines.asEnginesUiText(),
    fuelAmount = fuelAmountTons.asFuelUiText(),
    reusable = reusable.asReusableUiText(),
)

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
