package cz.quanti.rocketapp.multiplatform.feature.rocket.model

import com.goncalossilva.resources.Resource
import kotlinx.serialization.json.Json

private val json =
    Json {
        ignoreUnknownKeys = true
    }

internal val rocketsData: List<RocketData> =
    json.decodeFromString(
        Resource(path = "src/commonTest/resources/rockets.json").readText(),
    )
