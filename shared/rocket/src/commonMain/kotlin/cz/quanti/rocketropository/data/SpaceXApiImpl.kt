package cz.quanti.rocketropository.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SpaceXApiImpl(
    private val client: HttpClient,
) : SpaceXApi {
    override suspend fun listRockets(): List<RocketData> {
        return client.get("https://api.spacexdata.com/v4/rockets").body()
    }

    override suspend fun getRocket(id: String): RocketData {
        return client.get("https://api.spacexdata.com/v4/rockets/$id").body()
    }
}
