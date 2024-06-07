package cz.quanti.rocketapp.multiplatform.feature.rocket.data

internal interface SpaceXApi {
    suspend fun listRockets(): List<RocketData>

    suspend fun getRocket(id: String): RocketData
}
