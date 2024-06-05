package cz.quanti.rocketapp.util

import com.goncalossilva.resources.Resource
import cz.quanti.rocketropository.model.Rocket
import cz.quanti.rocketropository.model.RocketDetail
import kotlinx.serialization.json.Json

private val json =
    Json {
        ignoreUnknownKeys = true
    }

fun rocketsData(): List<Rocket> =
    json.decodeFromString(
        Resource(path = "src/test/resources/rocket.json").readText(),
    )

fun rocketsDetailData(): List<RocketDetail> =
    json.decodeFromString(
        Resource(path = "src/test/resources/rocketDetail.json").readText(),
    )
