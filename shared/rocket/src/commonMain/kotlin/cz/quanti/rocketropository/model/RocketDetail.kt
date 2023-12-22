package cz.quanti.rocketropository.model

import cz.quanti.rocketropository.data.RocketData
import cz.quanti.rocketropository.data.StageData

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
)

data class Stage(
    val burnTimeSec: Int?,
    val engines: Int,
    val fuelAmountTons: Double,
    val reusable: Boolean,
)

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
