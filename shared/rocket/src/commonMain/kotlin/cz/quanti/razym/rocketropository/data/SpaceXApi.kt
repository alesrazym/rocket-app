package cz.quanti.razym.rocketropository.data

interface SpaceXApi {
    suspend fun listRockets(): List<RocketData>

    suspend fun getRocket(id: String): RocketData
}