package cz.quanti.rocketropository.data

internal interface SpaceXApi {
    suspend fun listRockets(): List<RocketData>

    suspend fun getRocket(id: String): RocketData
}
