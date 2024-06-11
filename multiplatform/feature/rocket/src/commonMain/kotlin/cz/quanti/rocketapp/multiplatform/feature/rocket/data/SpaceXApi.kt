package cz.quanti.rocketapp.multiplatform.feature.rocket.data

import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketData

internal interface SpaceXApi {
    suspend fun listRockets(): List<RocketData>

    suspend fun getRocket(id: String): RocketData
}
