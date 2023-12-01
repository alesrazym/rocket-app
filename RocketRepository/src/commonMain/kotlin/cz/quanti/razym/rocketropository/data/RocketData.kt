package cz.quanti.razym.rocketropository.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketData(
    @SerialName("id") val id: String,
    @SerialName("description") val overview: String,
    @SerialName("diameter") val diameter: Map<String, Double>,
    @SerialName("first_flight") val firstFlight: String,
    @SerialName("first_stage") val firstStage: StageData,
    @SerialName("flickr_images") val flickrImages: List<String>,
    @SerialName("height") val height: Map<String, Double>,
    @SerialName("mass") val mass: Map<String, Double>,
    @SerialName("name") val name: String,
    @SerialName("second_stage") val secondStage: StageData,
) {
    companion object {
        const val METERS = "meters"
        const val KG = "kg"
    }
}

@Serializable
data class StageData(
    @SerialName("burn_time_sec") val burnTimeSec: Int?,
    @SerialName("engines") val engines: Int,
    @SerialName("fuel_amount_tons") val fuelAmountTons: Double,
    @SerialName("reusable") val reusable: Boolean
)
