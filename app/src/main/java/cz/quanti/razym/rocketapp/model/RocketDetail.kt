package cz.quanti.razym.rocketapp.model

import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.data.StageData

// TODO: do we prefer constructor or extension function?

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
    constructor(data: RocketData) : this(
        data.name,
        data.id,
        data.overview,
        data.height[RocketData.METERS] ?: 0.0,
        data.diameter[RocketData.METERS] ?: 0.0,
        (data.mass[RocketData.KG] ?: 0.0) / 1000.0,
        Stage(data.firstStage),
        Stage(data.secondStage),
        data.flickrImages,
    )
}

data class Stage(
    val burnTimeSec: Int?,
    val engines: Int,
    val fuelAmountTons: Double,
    val reusable: Boolean,
) {
    constructor(stage: StageData) : this(
        stage.burnTimeSec,
        stage.engines,
        stage.fuelAmountTons,
        stage.reusable,
    )
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
        this.flickrImages,
    )
}

fun StageData.asStage(): Stage {
    return Stage(this.burnTimeSec, this.engines, this.fuelAmountTons, this.reusable)
}
