package cz.quanti.rocketapp.android.feature.rocket.util

import com.goncalossilva.resources.Resource
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Rocket
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketDetail
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
