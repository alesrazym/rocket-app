package cz.quanti.rocketapp.multiplatform.feature.rocket.infrastructure

import com.goncalossilva.resources.Resource
import cz.quanti.rocketapp.multiplatform.feature.rocket.data.RocketData
import kotlinx.serialization.json.Json

private val json =
    Json {
        ignoreUnknownKeys = true
    }

internal val rocketsData: List<RocketData> =
    json.decodeFromString(
        Resource(path = "src/commonTest/resources/rockets.json").readText(),
    )
