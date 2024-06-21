package cz.quanti.rocketapp.multiplatform.feature.rocket.model

import kotlinx.serialization.Serializable

@Serializable
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

@Serializable
data class Stage(
    val burnTimeSec: Int?,
    val engines: Int,
    val fuelAmountTons: Double,
    val reusable: Boolean,
)
