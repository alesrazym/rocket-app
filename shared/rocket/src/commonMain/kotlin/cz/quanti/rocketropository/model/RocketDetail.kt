package cz.quanti.rocketropository.model

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
