package cz.quanti.razym.rocketapp.utils

import com.goncalossilva.resources.Resource
import cz.quanti.razym.rocketropository.data.RocketData
import kotlinx.serialization.json.Json

private val json =
    Json {
        ignoreUnknownKeys = true
    }

fun rocketsData(): List<RocketData> =
    json.decodeFromString(
        Resource(path = "src/test/resources/rockets.json").readText(),
    )
