package cz.quanti.razym.rocketapp.data

import com.squareup.moshi.Json

data class RocketData(
//    val active: Boolean,
//    val boosters: Int,
//    val company: String,
//    val cost_per_launch: Int,
//    val country: String,
//    val description: String,
//    val diameter: Diameter,
//    val engines: Engines,
    @Json(name = "first_flight") val firstFlight: String,
//    val first_stage: FirstStage,
//    val flickr_images: List<String>,
//    val height: Height,
//    val id: String,
//    val landing_legs: LandingLegs,
//    val mass: Mass,
    @Json(name = "name") val name: String,
//    val payload_weights: List<PayloadWeight>,
//    val second_stage: SecondStage,
//    val stages: Int,
//    val success_rate_pct: Int,
//    val type: String,
//    val wikipedia: String
)