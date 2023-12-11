package cz.quanti.razym.rocketapp.presentation

import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.model.RocketDetail as RocketDetailModel
import cz.quanti.razym.rocketapp.model.Stage as StageModel

// TODO: rename to RocketDetailUiState? (Stage as well)
data class RocketDetail(
    val name: String,
    val id: String,
    val overview: String,
    val heightMeters: Double,
    val diameterMeters: Double,
    val massTons: Double,
    val firstStage: Stage,
    val secondStage: Stage,
    val flickrImages: List<String>,
) {
    constructor(detail: RocketDetailModel) : this(
        detail.name,
        detail.id,
        detail.overview,
        detail.heightMeters,
        detail.diameterMeters,
        detail.massTons,
        Stage(detail.firstStage),
        Stage(detail.secondStage),
        detail.flickrImages,
    )
}

data class Stage(
    val burnTimeSec: UiText,
    val engines: UiText,
    val fuelAmount: UiText,
    val reusable: UiText,
) {
    constructor(stage: StageModel) : this(
        stage.burnTimeSec,
        stage.engines,
        stage.fuelAmountTons,
        stage.reusable,
    )

    // TODO: do we prefer constructor or extension function?
    //  In case of ctor, then extension function must be here (see below)

    constructor(
        burnTimeSec: Int?,
        engines: Int,
        fuelAmountTons: Double,
        reusable: Boolean,
    ) : this(
        burnTimeSec.asBurnUiText(),
        engines.asEnginesUiText(),
        fuelAmountTons.asFuelUiText(),
        reusable.asReusableUiText(),
    )
}

// TODO: This belongs here or to view model?
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
