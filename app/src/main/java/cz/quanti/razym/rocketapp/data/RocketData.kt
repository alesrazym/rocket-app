package cz.quanti.razym.rocketapp.data

import com.squareup.moshi.Json

data class RocketData(
    @Json(name = "id") val id: String,
    @Json(name = "description") val overview: String,
    @Json(name = "diameter") val diameter: Map<String, Double>,
    @Json(name = "first_flight") val firstFlight: String,
    @Json(name = "first_stage") val firstStage: StageData,
    @Json(name = "flickr_images") val flickrImages: List<String>,
    @Json(name = "height") val height: Map<String, Double>,
    @Json(name = "mass") val mass: Map<String, Double>,
    @Json(name = "name") val name: String,
    @Json(name = "second_stage") val secondStage: StageData,
) {
    companion object {
        const val METERS = "meters"
        const val KG = "kg"
    }
}

data class StageData(
    @Json(name = "burn_time_sec") val burnTimeSec: Int?,
    @Json(name = "engines") val engines: Int,
    @Json(name = "fuel_amount_tons") val fuelAmountTons: Double,
    @Json(name = "reusable") val reusable: Boolean
)
